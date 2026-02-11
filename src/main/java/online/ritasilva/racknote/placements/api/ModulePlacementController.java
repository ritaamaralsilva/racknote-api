package online.ritasilva.racknote.placements.api;

import online.ritasilva.racknote.placements.domain.ModulePlacementEntity;
import online.ritasilva.racknote.placements.service.ModulePlacementService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import jakarta.validation.Valid;
import java.util.UUID;
import java.util.List;

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
      ModulePlacementEntity saved =
          placementService.create(req.caseRowId(), req.moduleId(), req.xHp());

      return new PlacementResponse(
          saved.getId(),
          saved.getCaseRowId(),
          saved.getModuleId(),
          saved.getXHp()
      );

    } catch (IllegalArgumentException e) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
    }
  }

  @GetMapping
  public List<PlacementResponse> list(@RequestParam(name = "caseRowId") UUID caseRowId) {
    try {
      List<ModulePlacementEntity> placements = placementService.listByCaseRowId(caseRowId);

      return placements.stream()
          .map(p -> new PlacementResponse(p.getId(), p.getCaseRowId(), p.getModuleId(), p.getXHp()))
          .toList();

    } catch (IllegalArgumentException e) {
      if ("Case row not found".equals(e.getMessage())) {
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
      }
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
    }
  }


@DeleteMapping("/{placementId}")
@ResponseStatus(HttpStatus.NO_CONTENT)
public void delete(@PathVariable UUID placementId) {
  try {
    placementService.delete(placementId);
  } catch (IllegalArgumentException e) {
    if ("Placement not found".equals(e.getMessage())) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
    }
   throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
    }
  }

@PatchMapping(value = "/{placementId}", consumes = MediaType.APPLICATION_JSON_VALUE)
public PlacementResponse update(
    @PathVariable UUID placementId,
    @Valid @RequestBody UpdatePlacementRequest req
) {
  if (req == null || req.isEmpty()) {
    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Nothing to update");
  }

  try {
    ModulePlacementEntity updated =
        placementService.updatePosition(placementId, req.caseRowId(), req.xHp());

    return new PlacementResponse(
        updated.getId(),
        updated.getCaseRowId(),
        updated.getModuleId(),
        updated.getXHp()
    );

  } catch (IllegalArgumentException e) {
    // service uses IllegalArgumentException for client errors (bad request / not found)
    if ("Placement not found".equals(e.getMessage())) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
    }
    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);

  } catch (IllegalStateException e) {
    // service uses IllegalStateException for referential/data integrity problems
    throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage(), e);
  }
}
}
