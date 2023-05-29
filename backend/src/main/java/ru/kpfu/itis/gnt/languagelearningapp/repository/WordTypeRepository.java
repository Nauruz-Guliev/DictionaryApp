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

   @Query("select u from WordTypeEntity u where lower(u.text) like lower(concat('%', :text,'%'))")
   List<WordTypeEntity> findByTextCaseInsensitive(String text);
}
