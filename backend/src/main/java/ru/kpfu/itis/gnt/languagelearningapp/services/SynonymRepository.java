package ru.kpfu.itis.gnt.languagelearningapp.services;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.kpfu.itis.gnt.languagelearningapp.entities.SynonymEntity;

import java.util.Optional;

public interface SynonymRepository extends JpaRepository<SynonymEntity, Long> {

    Optional<SynonymEntity> findByTextIgnoreCase(String text);

}
