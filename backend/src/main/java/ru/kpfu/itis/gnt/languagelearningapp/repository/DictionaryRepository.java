package ru.kpfu.itis.gnt.languagelearningapp.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.kpfu.itis.gnt.languagelearningapp.entities.*;

import java.util.List;

@Repository
public interface DictionaryRepository extends JpaRepository<WordEntity, Long> {

    List<WordEntity> findByTextIgnoreCaseAndLocale(String text, LocaleEntity locale);

    List<WordEntity> findByTextIgnoreCase(String original);
    @Query("select u from WordEntity u WHERE u.type.text =:typeText AND lower(u.text) like lower(concat('%', :original,'%'))")
    WordEntity findByTextAndType(String original, String typeText);

    @Query("select w from WordEntity w where w.id in :ids")
    List<WordEntity> findByIds(@Param("ids") List<Long> ids);

    @Query(value = "SELECT exists(SELECT * FROM user_word WHERE user_id =:userId AND word_id =:wordId)", nativeQuery = true)
    boolean isFavorite(Long userId, Long wordId);

    @NonNull
    @Query("select w from WordEntity w where w.id in :ids")
    Page<WordEntity> findByIds(List<Long> ids, @NonNull Pageable pageable) throws DataAccessException;

}
