package online.ritasilva.racknote.modules.domain;

import jakarta.persistence.*;

import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "modules")
public class ModuleEntity {
    
    @Id
    @GeneratedValue
    @Column(nullable = false, updatable = false)
    private UUID id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String brand;

    @Column(name = "panel_height_u", nullable = false)
    private int panelHeightU;

    @Column(name = "panel_width_hp", nullable = false)
    private int panelWidthHp;

    @Column(name = "image_path", nullable = false)
    private String imagePath;

    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt;
    
    protected ModuleEntity() {
        // for JPA
    }

    public ModuleEntity(
        String name,
        String brand,
        int panelHeightU,
        int panelWidthHp,
        String imagePath
    ) {
        this.name = name;
        this.brand = brand;
        this.panelHeightU = panelHeightU;
        this.panelWidthHp = panelWidthHp;
        this.imagePath = imagePath;
        this.createdAt = OffsetDateTime.now();
    }
// getters
    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getBrand() {
        return brand;
    }

    public int getPanelWidthHp() {
        return panelWidthHp;
    }

    public int getPanelHeightU() {
        return panelHeightU;
    }
    
    public String getImagePath() {
        return imagePath;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }
}