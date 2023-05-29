package ru.kpfu.itis.gnt.languagelearningapp.entities;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@Transactional
@Entity
@Table(name = "meaning")
public class MeaningEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String text;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "word_id")
    private WordEntity word;

}
