package ru.kpfu.itis.gnt.languagelearningapp.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class TranslationDto {
    private String result;
    private boolean present;
    private byte[] audio;
}
