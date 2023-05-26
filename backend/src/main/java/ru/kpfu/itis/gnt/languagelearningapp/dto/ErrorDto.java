package ru.kpfu.itis.gnt.languagelearningapp.dto;


import lombok.*;

import java.util.Objects;

@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ErrorDto {

    private String status;
    private String message;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ErrorDto errorDto)) return false;
        return Objects.equals(getStatus(), errorDto.getStatus()) && Objects.equals(getMessage(), errorDto.getMessage());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getStatus(), getMessage());
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
