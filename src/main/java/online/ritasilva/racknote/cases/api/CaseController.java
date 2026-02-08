package online.ritasilva.racknote.cases.api;

import online.ritasilva.racknote.cases.domain.CaseEntity;
import online.ritasilva.racknote.cases.repo.CaseRepository;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/cases", produces = MediaType.APPLICATION_JSON_VALUE)
public class CaseController {

  private final CaseRepository caseRepository;

  public CaseController(CaseRepository caseRepository) {
    this.caseRepository = caseRepository;
  }

  @GetMapping("/main")
  public CaseLayoutResponse getMainCase() {
    CaseEntity c = caseRepository.findFirstByOrderByCreatedAtAsc()
        .orElseThrow(() -> new IllegalStateException("No case found"));

    return new CaseLayoutResponse(
        new CaseLayoutResponse.CaseResponse(
            c.getId(),
            c.getName(),
            c.getWidthHp()
        ),
        c.getRows().stream()
            .map(r -> new CaseLayoutResponse.CaseRowResponse(
                r.getId(),
                r.getRowIndex(),
                r.getHeightU()
            ))
            .toList()
    );
  }
}
