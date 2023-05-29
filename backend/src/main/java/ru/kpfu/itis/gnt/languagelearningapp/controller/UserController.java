package ru.kpfu.itis.gnt.languagelearningapp.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.tags.Tags;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import ru.kpfu.itis.gnt.languagelearningapp.constants.ApiEndPoints;
import ru.kpfu.itis.gnt.languagelearningapp.constants.ErrorMessageConstants;
import ru.kpfu.itis.gnt.languagelearningapp.dto.CredentialsDto;
import ru.kpfu.itis.gnt.languagelearningapp.dto.SignUpDto;
import ru.kpfu.itis.gnt.languagelearningapp.dto.UserDto;
import ru.kpfu.itis.gnt.languagelearningapp.dto.UserUpdateDto;
import ru.kpfu.itis.gnt.languagelearningapp.model.DictionaryModel;
import ru.kpfu.itis.gnt.languagelearningapp.model.ErrorModel;
import ru.kpfu.itis.gnt.languagelearningapp.model.SuccessModel;
import ru.kpfu.itis.gnt.languagelearningapp.model.dictionary.DictionaryItem;
import ru.kpfu.itis.gnt.languagelearningapp.repository.DictionaryRepository;
import ru.kpfu.itis.gnt.languagelearningapp.security.UserAuthProvider;
import ru.kpfu.itis.gnt.languagelearningapp.services.DictionaryService;
import ru.kpfu.itis.gnt.languagelearningapp.services.UserService;

import java.net.URI;

@RequiredArgsConstructor
@RestController
@RequestMapping()
@Tags(value = {
        @Tag(name = "Пользователи")
})
public class UserController {
    private final UserService service;
    private final DictionaryService dictionaryService;
    private final UserAuthProvider provider;

    @PostMapping(ApiEndPoints.Authentication.LOGIN)
    public ResponseEntity<UserDto> login(@RequestBody CredentialsDto credentialsDto) {
        UserDto userDto = service.login(credentialsDto);
        userDto.setToken(provider.createToken(userDto.getLogin()));
        return ResponseEntity.ok(userDto);
    }

    @DeleteMapping(ApiEndPoints.Authentication.PROFILE)
    public ResponseEntity<?> deleteUser(Authentication authentication) {
        if (authentication != null && authentication.isAuthenticated()) {
            UserDto userDto = (UserDto) authentication.getPrincipal();
            service.removeUser(userDto.getId());
            return ResponseEntity.ok(SuccessModel.builder().build());
        } else {
            return createNotAuthenticatedMessage();
        }
    }

    @PostMapping(ApiEndPoints.Authentication.REGISTER)
    public ResponseEntity<UserDto> register(@RequestBody SignUpDto signUpDto) {
        UserDto userDto = service.register(signUpDto);
        userDto.setToken(provider.createToken(userDto.getLogin()));
        return ResponseEntity.created(URI.create(ApiEndPoints.Authentication.PROFILE + userDto.getId()))
                .body(userDto);
    }

    @PutMapping(ApiEndPoints.Authentication.PROFILE)
    public ResponseEntity<?> updateUser(@RequestBody UserUpdateDto user) {
        return ResponseEntity.ok(service.updateUser(user));
    }

    @GetMapping(ApiEndPoints.Authentication.PROFILE)
    public ResponseEntity<?> getUserInfo(Authentication authentication, DictionaryRepository dictionaryRepository) {
        if (authentication != null && authentication.isAuthenticated()) {
            UserDto user = (UserDto) authentication.getPrincipal();
            return ResponseEntity.ok(service.getUser(user.getId()));
        } else {
            return createNotAuthenticatedMessage();
        }
    }

    private ResponseEntity<?> createNotAuthenticatedMessage() {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ErrorModel.builder()
                .message(
                        ErrorMessageConstants.NOT_AUTHORIZED.ERROR_MESSAGE
                ).status(HttpStatus.FORBIDDEN.toString()).build());
    }
}
