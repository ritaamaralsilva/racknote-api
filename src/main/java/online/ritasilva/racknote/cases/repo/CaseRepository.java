package online.ritasilva.racknote.cases.repo;

import online.ritasilva.racknote.cases.domain.CaseEntity;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface CaseRepository extends JpaRepository<CaseEntity, UUID> {

  @EntityGraph(attributePaths = "rows")
  Optional<CaseEntity> findFirstByOrderByCreatedAtAsc();
}
