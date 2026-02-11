package online.ritasilva.racknote.placements.api;

import jakarta.validation.constraints.Positive;

import java.util.UUID;

public record UpdatePlacementRequest(
        UUID caseRowId,
        @Positive Integer xHp
)   {
    public boolean isEmpty() {
        return caseRowId == null && xHp == null;    

    }
}
