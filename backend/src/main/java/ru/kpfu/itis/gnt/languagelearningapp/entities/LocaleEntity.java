package ru.kpfu.itis.gnt.languagelearningapp.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Transactional
@Data
@Entity
@Table(name = "locale")
public class LocaleEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String text;
    

}
