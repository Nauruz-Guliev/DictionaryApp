package ru.kpfu.itis.gnt.languagelearningapp.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@Entity
@Table(name = "word")
public class WordEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    private String text;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "locale_id")
    private LocaleEntity locale;

    @OneToMany(mappedBy = "word", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ImageUrlEntity> images = new ArrayList<>();

    @ManyToOne(cascade = CascadeType.REFRESH)
    @JoinColumn(name = "word_type_id")
    private WordTypeEntity type;

    @Column(name = "date_added")
    private Long dateAdded;


    @OneToMany(
            cascade = CascadeType.PERSIST,
            orphanRemoval = true,
            fetch = FetchType.EAGER
    )
    private List<SynonymEntity> synonyms;

    @OneToMany(
            cascade = CascadeType.PERSIST,
            orphanRemoval = true,
            fetch = FetchType.EAGER
    )
    private List<MeaningEntity> meanings;

}
