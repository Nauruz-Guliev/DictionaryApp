package ru.kpfu.itis.gnt.languagelearningapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.kpfu.itis.gnt.languagelearningapp.entities.UserEntity;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findByLogin(String login);

    @Transactional
    @Modifying
    @Query(value = "update UserEntity u set u.firstName = :first_name, u.lastName = :last_name, u.password =:password where u.id = :id")
    void updateUser(@Param("first_name") String firstName,
                    @Param("last_name") String lastName,
                    @Param("password") String password,
                    @Param("id") Long id);


    @Modifying(flushAutomatically = true)
    @Transactional
    @Query(value = "SELECT word_id FROM user_word WHERE user_id =:userId ", nativeQuery = true)
    List<Long> getFavoriteWordIds(Long userId);


}
