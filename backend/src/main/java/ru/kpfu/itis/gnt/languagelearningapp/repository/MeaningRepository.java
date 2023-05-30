package ru.kpfu.itis.gnt.languagelearningapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.kpfu.itis.gnt.languagelearningapp.entities.MeaningEntity;

import java.util.Optional;

@Repository
public interface MeaningRepository extends JpaRepository<MeaningEntity, Long> {
    Optional<MeaningEntity> findByTextIgnoreCase(String text);
}
