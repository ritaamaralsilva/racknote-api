package online.ritasilva.racknote.cases.domain;

import jakarta.persistence.*;
import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "case_rows")
public class CaseRowEntity {

  @Id
  @Column(nullable = false, updatable = false)
  private UUID id;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "case_id", nullable = false)
  private CaseEntity rackCase;

  @Column(name = "row_index", nullable = false)
  private Integer rowIndex;

  @Column(name = "height_u", nullable = false)
  private Integer heightU;

  @Column(name = "created_at", nullable = false)
  private OffsetDateTime createdAt;

  protected CaseRowEntity() {}

  public UUID getId() {
    return id;
  }

  public Integer getRowIndex() {
    return rowIndex;
  }

  public Integer getHeightU() {
    return heightU;
  }

  public OffsetDateTime getCreatedAt() {
    return createdAt;
  }

  public UUID getCaseId() {
    return rackCase.getId();
  }
}
