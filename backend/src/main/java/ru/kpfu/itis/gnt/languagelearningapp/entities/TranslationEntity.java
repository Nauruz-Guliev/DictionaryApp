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
@Table(name = "translation")public class TranslationEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String original;

    @Column(unique = true)
    private String translation;

    private byte[] audio;
    
}
