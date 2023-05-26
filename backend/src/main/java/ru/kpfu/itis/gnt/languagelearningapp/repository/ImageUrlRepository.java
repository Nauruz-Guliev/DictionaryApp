package ru.kpfu.itis.gnt.languagelearningapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.kpfu.itis.gnt.languagelearningapp.entities.ImageUrlEntity;
import ru.kpfu.itis.gnt.languagelearningapp.entities.WordEntity;

import java.util.List;
import java.util.Optional;

public interface ImageUrlRepository extends JpaRepository<ImageUrlEntity, Long> {

    Optional<ImageUrlEntity> findByUrl(String url);

    @Query("select i from ImageUrlEntity i where i.word =:word")
    List<ImageUrlEntity> findByWord(WordEntity word);
}
