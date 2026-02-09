package online.ritasilva.racknote.placements.domain;

import jakarta.persistence.*;
import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "module_placements")
public class ModulePlacementEntity {

  @Id
  @Column(nullable = false, updatable = false)
  private UUID id;

  @Column(name = "case_row_id", nullable = false)
  private UUID caseRowId;

  @Column(name = "module_id", nullable = false)
  private UUID moduleId;

  @Column(name = "x_hp", nullable = false)
  private Integer xHp;

  @Column(name = "created_at", nullable = false)
  private OffsetDateTime createdAt;

  protected ModulePlacementEntity() {}

  public ModulePlacementEntity(UUID caseRowId, UUID moduleId, Integer xHp) {
    this.caseRowId = caseRowId;
    this.moduleId = moduleId;
    this.xHp = xHp;
  }

  public UUID getId() { return id; }
  public UUID getCaseRowId() { return caseRowId; }
  public UUID getModuleId() { return moduleId; }
  public Integer getXHp() { return xHp; }
  public OffsetDateTime getCreatedAt() { return createdAt; }
}
