package online.ritasilva.racknote.cases.repo;

import online.ritasilva.racknote.cases.domain.CaseRowEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CaseRowRepository extends JpaRepository<CaseRowEntity, UUID> {}
