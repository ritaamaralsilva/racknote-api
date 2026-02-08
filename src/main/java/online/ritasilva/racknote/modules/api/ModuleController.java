package online.ritasilva.racknote.modules.api;

import online.ritasilva.racknote.modules.repo.ModuleRepository;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/modules")
public class ModuleController {

    private final ModuleRepository repo;

    public ModuleController(ModuleRepository repo) {
        this.repo = repo;
    }


    @GetMapping
    public List<ModuleResponse> list() {
        return repo.findAll().stream()
        .map(m -> new ModuleResponse(
            m.getId(),
            m.getName(),
            m.getBrand(),
            m.getPanelWidthHp(),
            m.getPanelHeightU(),
            m.getImagePath()
        ))
        .toList();
    }
}
