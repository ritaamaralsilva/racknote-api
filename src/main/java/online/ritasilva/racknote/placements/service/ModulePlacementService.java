package online.ritasilva.racknote.placements.service;

import online.ritasilva.racknote.cases.domain.CaseEntity;
import online.ritasilva.racknote.cases.domain.CaseRowEntity;
import online.ritasilva.racknote.cases.repo.CaseRepository;
import online.ritasilva.racknote.cases.repo.CaseRowRepository;
import online.ritasilva.racknote.modules.domain.ModuleEntity;
import online.ritasilva.racknote.modules.repo.ModuleRepository;
import online.ritasilva.racknote.placements.domain.ModulePlacementEntity;
import online.ritasilva.racknote.placements.repo.ModulePlacementRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
public class ModulePlacementService {

  private final ModulePlacementRepository placementRepository;
  private final CaseRowRepository caseRowRepository;
  private final CaseRepository caseRepository;
  private final ModuleRepository moduleRepository;

  public ModulePlacementService(
      ModulePlacementRepository placementRepository,
      CaseRowRepository caseRowRepository,
      CaseRepository caseRepository,
      ModuleRepository moduleRepository
  ) {
    this.placementRepository = placementRepository;
    this.caseRowRepository = caseRowRepository;
    this.caseRepository = caseRepository;
    this.moduleRepository = moduleRepository;
  }

  // READ (GET placements)

  @Transactional(readOnly = true)
  public List<ModulePlacementEntity> listByCaseRowId(UUID caseRowId) {
    Objects.requireNonNull(caseRowId, "caseRowId is required");

    if (!caseRowRepository.existsById(caseRowId)) {
      throw new IllegalArgumentException("Case row not found");
    }

    return placementRepository.findByCaseRowIdOrdered(caseRowId);
  }

  // CREATE (POST placement)

  @Transactional
  public ModulePlacementEntity create(UUID caseRowId, UUID moduleId, int xHp) {
    Objects.requireNonNull(caseRowId, "caseRowId is required");
    Objects.requireNonNull(moduleId, "moduleId is required");

    if (xHp <= 0) {
      throw new IllegalArgumentException("xHp must be > 0");
    }

    CaseRowEntity row = caseRowRepository.findById(caseRowId)
        .orElseThrow(() -> new IllegalArgumentException("Case row not found"));

    ModuleEntity module = moduleRepository.findById(moduleId)
        .orElseThrow(() -> new IllegalArgumentException("Module not found"));

    CaseEntity rackCase = loadCaseForRow(row);

    int caseWidth = rackCase.getWidthHp();
    int moduleWidth = module.getPanelWidthHp();

    validateWithinCaseWidth(xHp, moduleWidth, caseWidth);
    validateNoOverlap(caseRowId, null, xHp, moduleWidth);

    return placementRepository.save(new ModulePlacementEntity(caseRowId, moduleId, xHp));
  }

  // UPDATE (PATCH placement) — supports moving X and/or moving rows (within same case)

  @Transactional
  public ModulePlacementEntity updatePosition(UUID placementId, UUID newCaseRowId, Integer newXHp) {
    Objects.requireNonNull(placementId, "placementId is required");

    if (newCaseRowId == null && newXHp == null) {
      throw new IllegalArgumentException("Nothing to update");
    }

    ModulePlacementEntity placement = placementRepository.findById(placementId)
        .orElseThrow(() -> new IllegalArgumentException("Placement not found"));

    UUID currentRowId = Objects.requireNonNull(
        placement.getCaseRowId(),
        "ModulePlacement.caseRowId must not be null"
    );

    UUID moduleId = Objects.requireNonNull(
        placement.getModuleId(),
        "ModulePlacement.moduleId must not be null"
    );

    int currentXHp = placement.getXHp(); // primitive int in your entity usage

    // target state
    UUID targetRowId = (newCaseRowId != null) ? newCaseRowId : currentRowId;
    int targetXHp = (newXHp != null) ? newXHp : currentXHp;

    if (targetXHp <= 0) {
      throw new IllegalArgumentException("xHp must be > 0");
    }

    // load rows
    CaseRowEntity currentRow = caseRowRepository.findById(currentRowId)
        .orElseThrow(() -> new IllegalStateException("Case row not found"));

    CaseRowEntity targetRow = caseRowRepository.findById(targetRowId)
        .orElseThrow(() -> new IllegalStateException("Case row not found"));

    // forbid cross-case moves
    UUID currentCaseId = Objects.requireNonNull(currentRow.getCaseId(), "CaseRow.caseId must not be null");
    UUID targetCaseId = Objects.requireNonNull(targetRow.getCaseId(), "CaseRow.caseId must not be null");
    if (!currentCaseId.equals(targetCaseId)) {
      throw new IllegalArgumentException("Cannot move placement to a different case");
    }

    // module width + case width checks
    ModuleEntity module = moduleRepository.findById(moduleId)
        .orElseThrow(() -> new IllegalStateException("Placement refers to missing module"));

    CaseEntity rackCase = caseRepository.findById(targetCaseId)
        .orElseThrow(() -> new IllegalStateException("Case not found for row"));

    int caseWidth = rackCase.getWidthHp();
    int moduleWidth = module.getPanelWidthHp();

    validateWithinCaseWidth(targetXHp, moduleWidth, caseWidth);
    validateNoOverlap(targetRowId, placementId, targetXHp, moduleWidth);

    // apply
    placement.moveTo(targetRowId, targetXHp);

    return placementRepository.save(placement);
  }

  // DELETE (DELETE placement)

  @Transactional
  public void delete(UUID placementId) {
    Objects.requireNonNull(placementId, "placementId is required");

    if (!placementRepository.existsById(placementId)) {
      throw new IllegalArgumentException("Placement not found");
    }

    placementRepository.deleteById(placementId);
  }

  // -----------------------------
  // helpers
  // -----------------------------

  private CaseEntity loadCaseForRow(CaseRowEntity row) {
    UUID caseId = Objects.requireNonNull(row.getCaseId(), "CaseRow.caseId must not be null");
    return caseRepository.findById(caseId)
        .orElseThrow(() -> new IllegalStateException("Case not found for row"));
  }

  private void validateWithinCaseWidth(int xHp, int moduleWidth, int caseWidth) {
    int end = xHp + moduleWidth - 1;

    if (end > caseWidth) {
      throw new IllegalArgumentException("Placement overflows case width");
    }
    // (optional) if you ever allow xHp=0 you’d also validate start >= 0, but you currently enforce > 0
  }

  /**
   * Overlap check within a row.
   * @param excludePlacementId pass placementId when updating so we skip comparing the placement against itself
   */
    private void validateNoOverlap(UUID caseRowId, UUID excludePlacementId, int xHp, int moduleWidth) {
    List<ModulePlacementEntity> existing = placementRepository.findByCaseRowIdOrdered(caseRowId);

    int start = xHp;
    int end = xHp + moduleWidth - 1;

    for (ModulePlacementEntity p : existing) {
      if (excludePlacementId != null && excludePlacementId.equals(p.getId())) {
        continue;
      }

      UUID placedModuleId = Objects.requireNonNull(
          p.getModuleId(),
          "ModulePlacement.moduleId must not be null"
      );

      ModuleEntity placedModule = moduleRepository.findById(placedModuleId)
          .orElseThrow(() -> new IllegalStateException("Existing placement refers to missing module"));

      int pStart = p.getXHp();
      int pEnd = p.getXHp() + placedModule.getPanelWidthHp() - 1;

      boolean overlaps = !(end < pStart || start > pEnd);
      if (overlaps) {
        throw new IllegalArgumentException("Placement overlaps an existing module");
      }
    }
  }
}