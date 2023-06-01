package ru.kpfu.itis.gnt.languagelearningapp.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.tags.Tags;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.kpfu.itis.gnt.languagelearningapp.constants.ApiEndPoints;
import ru.kpfu.itis.gnt.languagelearningapp.model.TranslationDto;
import ru.kpfu.itis.gnt.languagelearningapp.services.TranslationService;

@RestController
@RequestMapping(value = "api/v1/")
@Tags(value = {
        @Tag(name = "Перевод слов")
})
@RequiredArgsConstructor
public class TranslationController {
    private final TranslationService translationService;

    @Operation(summary = "Перевод")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Перевод слов",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = TranslationDto.class))
                    }
            )
    })
    @PostMapping(ApiEndPoints.TRANSLATE)
    public ResponseEntity<TranslationDto> getTranslation(@RequestBody ru.kpfu.itis.gnt.languagelearningapp.dto.TranslationDto translationDto) {

        return ResponseEntity.ok(translationService.getTranslation(
                translationDto
        ));
    }
}
