package online.ritasilva.racknote.placements.api;

import online.ritasilva.racknote.placements.domain.ModulePlacementEntity;
import online.ritasilva.racknote.placements.service.ModulePlacementService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import jakarta.validation.Valid;

import java.util.Objects;
import java.util.UUID;

@RestController
@RequestMapping(value = "/api/placements", produces = MediaType.APPLICATION_JSON_VALUE)
public class ModulePlacementController {

  private final ModulePlacementService placementService;

  public ModulePlacementController(ModulePlacementService placementService) {
    this.placementService = placementService;
  }

  @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.CREATED)
  public PlacementResponse create(@Valid @RequestBody CreatePlacementRequest req) {
    try {
      UUID caseRowId = Objects.requireNonNull(req.caseRowId(), "caseRowId is required");
      UUID moduleId  = Objects.requireNonNull(req.moduleId(), "moduleId is required");

      ModulePlacementEntity saved =
          placementService.create(caseRowId, moduleId, req.xHp());

      return new PlacementResponse(
          saved.getId(),
          saved.getCaseRowId(),
          saved.getModuleId(),
          saved.getXHp()
      );

    } catch (IllegalArgumentException e) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
    }
  }
}
