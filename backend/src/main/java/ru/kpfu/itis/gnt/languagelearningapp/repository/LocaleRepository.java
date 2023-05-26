package ru.kpfu.itis.gnt.languagelearningapp.repository;

import jakarta.persistence.*;
import jakarta.persistence.criteria.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.kpfu.itis.gnt.languagelearningapp.entities.LocaleEntity;
import ru.kpfu.itis.gnt.languagelearningapp.entities.TranslationEntity;
import ru.kpfu.itis.gnt.languagelearningapp.entities.WordEntity;
import ru.kpfu.itis.gnt.languagelearningapp.exception.DatabaseException;

import java.util.Locale;
import java.util.Optional;

@Repository
public interface LocaleRepository extends JpaRepository<LocaleEntity, Long> {

    @Query("select u from LocaleEntity u where lower(u.text) like lower(concat('%', :text,'%'))")
    Optional<LocaleEntity> findByTextCaseInsensitive(String text);
}
