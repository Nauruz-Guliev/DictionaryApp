package ru.kpfu.itis.gnt.languagelearningapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.kpfu.itis.gnt.languagelearningapp.entities.LocaleEntity;

import java.util.Optional;

@Repository
public interface LocaleRepository extends JpaRepository<LocaleEntity, Long> {

    Optional<LocaleEntity> findByTextIgnoreCase(String text);
}
