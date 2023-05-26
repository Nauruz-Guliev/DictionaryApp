package ru.kpfu.itis.gnt.languagelearningapp.repository;

import lombok.RequiredArgsConstructor;
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
import java.util.Optional;

@Repository
@Transactional
public interface WordRepository extends JpaRepository<WordEntity, Long> {
    List<WordEntity> findByTextAndLocale(String text, LocaleEntity locale);




    @Query("select u from WordEntity u where lower(u.text) like lower(concat('%', :original,'%'))")
    List<WordEntity> findByTextCaseInsensitive(String original);

    @Transactional
    @Query("select w from WordEntity w where w.id in :ids")
    List<WordEntity> findByIds(@Param("ids") List<Long> ids);


    @Transactional(readOnly = true)
    @NonNull
    @Query("select w from WordEntity w where w.id in :ids")
    Page<WordEntity> findByIds(List<Long> ids, @NonNull Pageable pageable) throws DataAccessException;



}
