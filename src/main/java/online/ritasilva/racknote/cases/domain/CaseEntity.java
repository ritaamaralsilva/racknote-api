package online.ritasilva.racknote.cases.domain;

import jakarta.persistence.*;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "cases")
public class CaseEntity {

  @Id
  @Column(nullable = false, updatable = false)
  private UUID id;

  @Column(nullable = false)
  private String name;

  @Column(name = "width_hp", nullable = false)
  private Integer widthHp;

  @Column(name = "created_at", nullable = false)
  private OffsetDateTime createdAt;

  @OneToMany(mappedBy = "rackCase", fetch = FetchType.LAZY)
  @OrderBy("rowIndex ASC")
  private List<CaseRowEntity> rows = new ArrayList<>();

  protected CaseEntity() {}

  public UUID getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public Integer getWidthHp() {
    return widthHp;
  }

  public OffsetDateTime getCreatedAt() {
    return createdAt;
  }

  public List<CaseRowEntity> getRows() {
    return rows;
  }
}

