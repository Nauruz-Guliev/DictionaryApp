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
import ru.kpfu.itis.gnt.languagelearningapp.api.translation.models.TranslationResponse;
import ru.kpfu.itis.gnt.languagelearningapp.constants.ApiEndPoints;
import ru.kpfu.itis.gnt.languagelearningapp.dto.CredentialsDto;
import ru.kpfu.itis.gnt.languagelearningapp.dto.TranslationDto;
import ru.kpfu.itis.gnt.languagelearningapp.model.TranslationModel;
import ru.kpfu.itis.gnt.languagelearningapp.services.TranslationService;

import java.util.Locale;

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
                                    schema = @Schema(implementation = TranslationModel.class))
                    }
            )
    })
    @PostMapping(ApiEndPoints.TRANSLATE)
    public ResponseEntity<TranslationModel> getTranslation(@RequestBody TranslationDto translationDto) {
        return ResponseEntity.ok(translationService.getTranslation(
                translationDto
        ));
    }
}
