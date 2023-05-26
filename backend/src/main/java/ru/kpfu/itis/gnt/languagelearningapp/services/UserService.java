package ru.kpfu.itis.gnt.languagelearningapp.services;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.kpfu.itis.gnt.languagelearningapp.constants.ErrorMessageConstants;
import ru.kpfu.itis.gnt.languagelearningapp.dto.CredentialsDto;
import ru.kpfu.itis.gnt.languagelearningapp.dto.SignUpDto;
import ru.kpfu.itis.gnt.languagelearningapp.dto.UserDto;
import ru.kpfu.itis.gnt.languagelearningapp.dto.UserUpdateDto;
import ru.kpfu.itis.gnt.languagelearningapp.entities.UserEntity;
import ru.kpfu.itis.gnt.languagelearningapp.exception.AuthenticationException;
import ru.kpfu.itis.gnt.languagelearningapp.exception.InvalidFieldDataException;
import ru.kpfu.itis.gnt.languagelearningapp.mappers.mapstruct.UserMapper;
import ru.kpfu.itis.gnt.languagelearningapp.repository.UserRepository;
import ru.kpfu.itis.gnt.languagelearningapp.repository.WordRepository;

import java.nio.CharBuffer;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder encoder;
    private final WordRepository wordRepository;

    public UserDto findByLogin(String login) {
        UserEntity userEntity = userRepository.findByLogin(login)
                .orElseThrow(() -> new AuthenticationException(ErrorMessageConstants.NOT_AUTHORIZED.USER_NOT_FOUND, HttpStatus.NOT_FOUND));
        return userMapper.toUserDto(userEntity);
    }

    public UserDto updateUser(UserUpdateDto updateDto) {
        if (updateDto.getPassword() == null || updateDto.getPasswordRepeat() == null || updateDto.getPasswordRepeat().length() <= 1 || updateDto.getPassword().length() <= 1 || !updateDto.getPassword().equals(updateDto.getPasswordRepeat()) || updateDto.getId() == null) {
            throw new InvalidFieldDataException(ErrorMessageConstants.INVALID_DATA.PASSWORDS_NOT_EQUAL);
        }
        Optional<UserEntity> optionalUserEntity = userRepository.findByLogin(updateDto.getLogin());
        if (optionalUserEntity.isEmpty()) {
            throw new AuthenticationException(ErrorMessageConstants.NOT_AUTHORIZED.ERROR_MESSAGE, HttpStatus.BAD_REQUEST);
        }

        userRepository.updateUser(
                updateDto.getFirstName(),
                updateDto.getLastName(),
                encoder.encode(CharBuffer.wrap(updateDto.getPassword())),
                updateDto.getId()
        );
        return userMapper.toUserDto(userRepository.findById(updateDto.getId()).orElseThrow(
                () -> new AuthenticationException(ErrorMessageConstants.NOT_FOUND.LOCAL, HttpStatus.BAD_REQUEST)
        ));
    }

    public UserDto getUser(Long id) {
        UserEntity userEntity = userRepository.findById(id)
                .orElseThrow(() -> new AuthenticationException(ErrorMessageConstants.NOT_AUTHORIZED.USER_NOT_FOUND, HttpStatus.NOT_FOUND));
        return userMapper.toUserDto(userEntity);
    }

    public UserDto login(CredentialsDto credentialsDto) {
        UserEntity userEntity = userRepository.findByLogin(credentialsDto.getLogin())
                .orElseThrow(() -> new AuthenticationException(ErrorMessageConstants.NOT_AUTHORIZED.USER_NOT_FOUND, HttpStatus.NOT_FOUND));
        if (encoder.matches(CharBuffer.wrap(credentialsDto.getPassword()), userEntity.getPassword())) {
            return userMapper.toUserDto(userEntity);
        }
        throw new AuthenticationException(ErrorMessageConstants.NOT_AUTHORIZED.INVALID_PASSWORD, HttpStatus.BAD_REQUEST);
    }

    public void removeUser(Long id) {
        userRepository.findById(id)
                .orElseThrow(() -> new AuthenticationException(ErrorMessageConstants.NOT_FOUND.USER, HttpStatus.NOT_FOUND));
        userRepository.deleteById(id);
    }

    public UserDto register(SignUpDto signUpDto) {
        Optional<UserEntity> optionalUserEntity = userRepository.findByLogin(signUpDto.getLogin());
        if (optionalUserEntity.isPresent()) {
            throw new AuthenticationException(ErrorMessageConstants.NOT_AUTHORIZED.EXISTING_LOGIN, HttpStatus.BAD_REQUEST);
        }
        UserEntity userEntity = userMapper.toUserEntity(signUpDto);
        userEntity.setPassword(encoder.encode(CharBuffer.wrap(signUpDto.getPassword())));
        UserEntity savedUser = userRepository.save(userEntity);
        return userMapper.toUserDto(savedUser);
    }

}
