package ru.kpfu.itis.gnt.languagelearningapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.kpfu.itis.gnt.languagelearningapp.entities.TranslationEntity;
import ru.kpfu.itis.gnt.languagelearningapp.entities.UserEntity;

import java.util.Optional;

@Repository
public interface TranslationRepository extends JpaRepository<TranslationEntity, Long> {

    @Query("select u from TranslationEntity u where lower(u.original) like lower(concat('%', :original,'%'))")
    Optional<TranslationEntity> findByTextCaseInsensitive(String original);
}
