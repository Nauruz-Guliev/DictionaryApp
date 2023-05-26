package ru.kpfu.itis.gnt.languagelearningapp.dto;

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
