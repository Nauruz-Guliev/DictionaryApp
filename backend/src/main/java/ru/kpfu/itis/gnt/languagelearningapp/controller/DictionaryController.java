package ru.kpfu.itis.gnt.languagelearningapp.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.tags.Tags;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import ru.kpfu.itis.gnt.languagelearningapp.constants.ApiEndPoints;
import ru.kpfu.itis.gnt.languagelearningapp.constants.ErrorMessageConstants;
import ru.kpfu.itis.gnt.languagelearningapp.dto.TranslationDto;
import ru.kpfu.itis.gnt.languagelearningapp.dto.UserDto;
import ru.kpfu.itis.gnt.languagelearningapp.dto.DictionaryDto;
import ru.kpfu.itis.gnt.languagelearningapp.dto.ErrorDto;
import ru.kpfu.itis.gnt.languagelearningapp.model.PaginationDto;
import ru.kpfu.itis.gnt.languagelearningapp.model.dictionary.DictionaryItem;
import ru.kpfu.itis.gnt.languagelearningapp.repository.DictionaryRepository;
import ru.kpfu.itis.gnt.languagelearningapp.services.DictionaryService;

import java.util.List;

@RestController
@RequestMapping("api/v1/")
@Tags(value = {
        @Tag(name = "Словарь")
})
@RequiredArgsConstructor
public class DictionaryController {
    private final DictionaryService dictionaryService;
    private final DictionaryRepository repository;



    @Operation(summary = "Словарь")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Словарное значение",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = DictionaryDto.class))
                    }
            )
    })
    @PostMapping(ApiEndPoints.DICTIONARY)
    public ResponseEntity<?> getDictionary(@RequestBody TranslationDto dto, Authentication authentication) {
        System.err.println(repository.findWordWithMostMeanings());
        if (authentication != null && authentication.isAuthenticated()) {
            DictionaryDto dictionaryDto = dictionaryService.getDictionaryModel(
                    dto, (UserDto) authentication.getPrincipal()
            );
            return ResponseEntity.ok(dictionaryDto);
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ErrorDto.builder().status(HttpStatus.FORBIDDEN.toString()).message(ErrorMessageConstants.NOT_AUTHORIZED.ERROR_MESSAGE));
        }
    }

    @GetMapping(ApiEndPoints.FAVORITES)
    public ResponseEntity<?> getFavorites(Authentication authentication) {
        if (authentication != null && authentication.isAuthenticated()) {
            List<DictionaryItem> dictionaryItems = dictionaryService.getUserWordEntityList(
                    (UserDto) authentication.getPrincipal()
            );
            return ResponseEntity.ok(dictionaryItems);
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ErrorDto.builder().status(HttpStatus.FORBIDDEN.toString()).message(ErrorMessageConstants.NOT_AUTHORIZED.ERROR_MESSAGE));
        }
    }

    @PostMapping(ApiEndPoints.FAVORITES)
    public ResponseEntity<?> getFavoritesPaginated(Authentication authentication,
                                                   @RequestBody ru.kpfu.itis.gnt.languagelearningapp.dto.PaginationDto model) {
        if (authentication != null && authentication.isAuthenticated()) {
            Page<DictionaryItem> paginated = findPaginated(model.getPage(), model.getSize(), (UserDto) authentication.getPrincipal());
            return ResponseEntity.ok(createModel(model.getPage(), paginated));
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ErrorDto.builder().status(HttpStatus.FORBIDDEN.toString()).message(ErrorMessageConstants.NOT_AUTHORIZED.ERROR_MESSAGE));
        }
    }

    @PostMapping(ApiEndPoints.Favorites.ADD)
    public ResponseEntity<?> setFavorite(Authentication authentication,
                                         @RequestBody DictionaryItem model) {
        if (authentication == null || authentication.getPrincipal() == null) {
            return createNotAuthenticatedMessage();
        } else {
            return ResponseEntity.ok(dictionaryService.addFavorite(model, (UserDto) authentication.getPrincipal()));
        }
    }


    @PostMapping(ApiEndPoints.Favorites.REMOVE)
    public ResponseEntity<?> deleteFavorite(Authentication authentication,
                                            @RequestBody DictionaryItem model) {
        if (authentication == null || authentication.getPrincipal() == null) {
            return createNotAuthenticatedMessage();
        } else {
            return ResponseEntity.ok(dictionaryService.deleteFavorite(model, (UserDto) authentication.getPrincipal()));
        }
    }

    @GetMapping(ApiEndPoints.WORD)
    public ResponseEntity<String> getWord() {
        return ResponseEntity.ok(dictionaryService.getWord());
    }

    private Page<DictionaryItem> findPaginated(int page, int pageSize, UserDto userDto) {
        Pageable pageable = PageRequest.of(page, pageSize <= 1 ? 5 : pageSize);
        return dictionaryService.getUserWordEntityList(pageable, userDto);
    }

    private PaginationDto createModel(int page, Page<DictionaryItem> paginated) {
        return PaginationDto.builder()
                .currentPage((long) page)
                .totalPages((long) paginated.getTotalPages())
                .itemPage(paginated.getContent())
                .totalElements(paginated.getTotalElements())
                .build();
    }



    private ResponseEntity<?> createNotAuthenticatedMessage() {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ErrorDto.builder()
                .message(
                        ErrorMessageConstants.NOT_AUTHORIZED.ERROR_MESSAGE
                ).status(HttpStatus.FORBIDDEN.toString()).build());
    }

}
