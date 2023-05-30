package ru.kpfu.itis.gnt.languagelearningapp.dto;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Locale;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class TranslationDto {
    private String text;
    private Locale from;
    private Locale to;
}
