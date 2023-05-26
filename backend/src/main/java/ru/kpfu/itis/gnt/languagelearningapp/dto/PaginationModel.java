package ru.kpfu.itis.gnt.languagelearningapp.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class PaginationModel {
    private int page;
    private int size;
}
