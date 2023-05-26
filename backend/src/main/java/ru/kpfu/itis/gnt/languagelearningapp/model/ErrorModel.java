package ru.kpfu.itis.gnt.languagelearningapp.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ErrorModel {
    private String status;
    private String message;
}
