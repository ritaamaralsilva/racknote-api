package online.ritasilva.racknote.placements.api;

import java.util.UUID;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record CreatePlacementRequest(
    @NotNull UUID caseRowId,
    @NotNull UUID moduleId,
    @Positive int xHp
) {}