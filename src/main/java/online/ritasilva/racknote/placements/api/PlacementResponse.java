package online.ritasilva.racknote.placements.api;

import java.util.UUID;

public record PlacementResponse(UUID id, UUID caseRowId, UUID moduleId, int xHp) {}
