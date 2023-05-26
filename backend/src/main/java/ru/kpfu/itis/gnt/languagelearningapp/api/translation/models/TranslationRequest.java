package ru.kpfu.itis.gnt.languagelearningapp.api.translation.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class TranslationRequest {
    private String translateMode = "html";
    private String platform = "api";
    private String from;
    private String to;
    private String data;
}
