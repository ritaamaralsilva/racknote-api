package online.ritasilva.racknote.cases.api;

import java.util.List;
import java.util.UUID;

public record CaseLayoutResponse(
    CaseResponse rackCase,
    List<CaseRowResponse> rows
) {
  public record CaseResponse(UUID id, String name, int widthHp) {}
  public record CaseRowResponse(UUID id, int rowIndex, int heightU) {}
}
