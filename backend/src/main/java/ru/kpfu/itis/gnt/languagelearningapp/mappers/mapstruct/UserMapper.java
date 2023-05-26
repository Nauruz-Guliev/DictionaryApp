package ru.kpfu.itis.gnt.languagelearningapp.mappers.mapstruct;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.kpfu.itis.gnt.languagelearningapp.dto.SignUpDto;
import ru.kpfu.itis.gnt.languagelearningapp.dto.UserDto;
import ru.kpfu.itis.gnt.languagelearningapp.dto.UserUpdateDto;
import ru.kpfu.itis.gnt.languagelearningapp.entities.MeaningEntity;
import ru.kpfu.itis.gnt.languagelearningapp.entities.SynonymEntity;
import ru.kpfu.itis.gnt.languagelearningapp.entities.UserEntity;
import ru.kpfu.itis.gnt.languagelearningapp.mappers.custom.DictionaryMapper;
import ru.kpfu.itis.gnt.languagelearningapp.model.dictionary.DictionaryItem;

import java.util.HashSet;

@Component
@RequiredArgsConstructor
public class UserMapper {

    private final DictionaryMapper mapper;

    public UserDto toUserDto(UserEntity userEntity) {
        return UserDto.builder()
                .login(userEntity.getLogin())
                .id(userEntity.getId())
                .lastName(userEntity.getLastName())
                .firstName(userEntity.getFirstName())
                .words(userEntity.getWords() != null ? (new HashSet<>(userEntity.getWords().stream().map( wordEntity ->
                        mapper.mapTo(wordEntity, false)
                ).toList())) : new HashSet<>())
                .build();

    }
    /*

    public UserEntity toUserEntity(UserDto userDto) {
        return UserEntity.builder()
                .login(userDto.getLogin())
                .id(userDto.getId())
                .lastName(userDto.getLastName())
                .firstName(userDto.getFirstName())
                .words(userDto.getWords().stream().map(

                ))
                .build();



    }

     */

    public UserEntity toUserEntity(SignUpDto signUpDto) {
        return UserEntity.builder()
                .lastName(signUpDto.getLastName())
                .firstName(signUpDto.getFirstName())
                .login(signUpDto.getLogin())
                .build();
    }

    public UserEntity toUserEntity(UserUpdateDto updateDto) {
        return UserEntity.builder()
                .login(updateDto.getLogin())
                .firstName(updateDto.getFirstName())
                .lastName(updateDto.getLastName())
                .build();

    }


}
