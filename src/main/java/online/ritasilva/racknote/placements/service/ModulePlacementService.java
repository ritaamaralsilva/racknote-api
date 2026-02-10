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

  // READ (GET placements) vvv

  @Transactional(readOnly = true)
  public List<ModulePlacementEntity> listByCaseRowId(UUID caseRowId) {
    Objects.requireNonNull(caseRowId, "caseRowId is required");

    if (!caseRowRepository.existsById(caseRowId)) {
      throw new IllegalArgumentException("Case row not found");
    }

    return placementRepository.findByCaseRowIdOrdered(caseRowId);
  }


  // CREATE (POST placement) vvv

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
    
        UUID caseId = Objects.requireNonNull(
            row.getCaseId(),
            "CaseRow.caseId must not be null"
        );
        
        CaseEntity rackCase = caseRepository.findById(caseId)
            .orElseThrow(() -> new IllegalStateException("Case not found for row"));
        
        
    int caseWidth = rackCase.getWidthHp();      // Integer auto-unboxed
    int moduleWidth = module.getPanelWidthHp();

    int start = xHp;
    int end = xHp + moduleWidth - 1;

    if (end > caseWidth) {
      throw new IllegalArgumentException("Placement overflows case width");
    }

    // Overlap check within same row
    List<ModulePlacementEntity> existing = placementRepository.findByCaseRowIdOrdered(caseRowId);

    for (ModulePlacementEntity p : existing) {
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

    return placementRepository.save(new ModulePlacementEntity(caseRowId, moduleId, xHp));
  }

// DELETE (DELETE placement) vvv

@Transactional
public void delete(UUID placementId) {
  if (placementId == null) {
    throw new IllegalArgumentException("placementId is required");    
  }
  if (!placementRepository.existsById(placementId)) {
    throw new IllegalArgumentException("Placement not found");
  }

  placementRepository.deleteById(placementId);
  }
}