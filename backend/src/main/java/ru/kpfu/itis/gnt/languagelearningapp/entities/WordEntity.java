package ru.kpfu.itis.gnt.languagelearningapp.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@AllArgsConstructor
@Transactional
@NoArgsConstructor
@Builder
@Data
@Entity
@Table(name = "word")
public class WordEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String text;

    @ManyToOne
    @JoinColumn(name = "locale_id")
    private LocaleEntity locale;

    @OneToMany(mappedBy = "word", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ImageUrlEntity> images = new ArrayList<>();

    @Column(name = "date_added")
    private Long dateAdded;


    @ManyToMany
    @JoinTable(
            name = "user_word",
            joinColumns = @JoinColumn(name = "word_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private Set<UserEntity> users = new HashSet<>();


    @OneToMany(mappedBy = "word", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<SynonymEntity> synonyms;

    @OneToMany(mappedBy = "word", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<MeaningEntity> meanings;

}
