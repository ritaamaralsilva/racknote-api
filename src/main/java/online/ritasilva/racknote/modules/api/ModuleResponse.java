package online.ritasilva.racknote.modules.api;
import java.util.UUID;

public record ModuleResponse(
    UUID id,
    String name,
    String brand,
    int panelWidthHp,
    int panelHeightU,
    String imagePath
) {}