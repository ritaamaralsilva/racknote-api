package online.ritasilva.racknote.modules.repo;

import online.ritasilva.racknote.modules.domain.ModuleEntity;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ModuleRepository extends JpaRepository<ModuleEntity, UUID> {}