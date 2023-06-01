package ru.kpfu.itis.gnt.languagelearningapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.kpfu.itis.gnt.languagelearningapp.entities.LocaleEntity;
import ru.kpfu.itis.gnt.languagelearningapp.entities.WordTypeEntity;

import java.util.List;
import java.util.Optional;

@Repository
public interface WordTypeRepository extends JpaRepository<WordTypeEntity, Long> {


   Optional<WordTypeEntity> findByTextIgnoreCase(String text);
}
