package online.ritasilva.racknote.placements.domain;

import jakarta.persistence.*;
import java.time.OffsetDateTime;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "module_placements")
public class ModulePlacementEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  @Column(nullable = false, updatable = false)
  private UUID id;

  @Column(name = "case_row_id", nullable = false)
  private UUID caseRowId;

  @Column(name = "module_id", nullable = false)
  private UUID moduleId;

  @Column(name = "x_hp", nullable = false)
  private int xHp;

  @Column(name = "created_at", nullable = false)
  private OffsetDateTime createdAt;

  protected ModulePlacementEntity() {}

  public ModulePlacementEntity(UUID caseRowId, UUID moduleId, int xHp) {
    this.caseRowId = Objects.requireNonNull(caseRowId, "caseRowId is required");
    this.moduleId = Objects.requireNonNull(moduleId, "moduleId is required");
    setXHp(xHp);
  }

  @PrePersist
  void prePersist() {
    if (createdAt == null) createdAt = OffsetDateTime.now();
  }

  // Domain update for PATCH (move a module within the row or to another row)
  public void moveTo(UUID newCaseRowId, int newXHp) {
    this.caseRowId = Objects.requireNonNull(newCaseRowId, "caseRowId is required");
    setXHp(newXHp);
  }
  
  private void setXHp(int value) {
    if (value <= 0) {
      throw new IllegalArgumentException("xHp must be > 0");
    }
    this.xHp = value;
  }

  public UUID getId() { return id; }
  public UUID getCaseRowId() { return caseRowId; }
  public UUID getModuleId() { return moduleId; }
  public int getXHp() { return xHp; }
  public OffsetDateTime getCreatedAt() { return createdAt; }
}
