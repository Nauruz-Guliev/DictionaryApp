package ru.kpfu.itis.gnt.languagelearningapp.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;
import ru.kpfu.itis.gnt.languagelearningapp.model.dictionary.DictionaryItem;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class PaginationModel {

    private Long totalPages;
    private Long totalElements;
    private List<DictionaryItem> itemPage;
    private Long currentPage;
}
