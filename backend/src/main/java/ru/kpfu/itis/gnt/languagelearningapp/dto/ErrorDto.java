package ru.kpfu.itis.gnt.languagelearningapp.dto;


import lombok.*;

import java.util.Objects;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class ErrorDto {

    private String status;
    private String message;

}
