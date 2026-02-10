package online.ritasilva.racknote.placements.repo;

import online.ritasilva.racknote.placements.domain.ModulePlacementEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


import java.util.List;
import java.util.UUID;

public interface ModulePlacementRepository
extends JpaRepository<ModulePlacementEntity, UUID> {
  @Query("select p from ModulePlacementEntity p where p.caseRowId = :caseRowId order by p.xHp asc")
  List<ModulePlacementEntity> findByCaseRowIdOrdered (@Param("caseRowId") UUID caseRowId);
}
