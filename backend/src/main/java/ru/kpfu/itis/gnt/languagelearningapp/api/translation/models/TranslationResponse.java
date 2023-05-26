package ru.kpfu.itis.gnt.languagelearningapp.api.translation.models;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class TranslationResponse {
    public Object err;
    public String result;
}
