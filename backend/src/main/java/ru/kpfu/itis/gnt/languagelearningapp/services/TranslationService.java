package ru.kpfu.itis.gnt.languagelearningapp.services;


import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import retrofit2.Retrofit;
import ru.kpfu.itis.gnt.languagelearningapp.api.dictionary.DictionaryApi;
import ru.kpfu.itis.gnt.languagelearningapp.api.dictionary.models.YandexDictionaryResponse;
import ru.kpfu.itis.gnt.languagelearningapp.api.translation.TranslationApi;
import ru.kpfu.itis.gnt.languagelearningapp.api.translation.models.TranslationRequest;
import ru.kpfu.itis.gnt.languagelearningapp.api.translation.models.TranslationResponse;
import ru.kpfu.itis.gnt.languagelearningapp.constants.ErrorMessageConstants;
import ru.kpfu.itis.gnt.languagelearningapp.constants.QualifierConstants;
import ru.kpfu.itis.gnt.languagelearningapp.entities.LocaleEntity;
import ru.kpfu.itis.gnt.languagelearningapp.entities.TranslationEntity;
import ru.kpfu.itis.gnt.languagelearningapp.exception.ResourceNotFoundException;
import ru.kpfu.itis.gnt.languagelearningapp.model.TranslationDto;
import ru.kpfu.itis.gnt.languagelearningapp.repository.LocaleRepository;
import ru.kpfu.itis.gnt.languagelearningapp.repository.TranslationRepository;

import java.util.Locale;
import java.util.Optional;

@Service
public class TranslationService {
    private final Retrofit translationRetrofit;
    private final Retrofit dictionaryRetrofit;
    private final AudioService audioService;
    private final LocaleRepository localeRepository;
    private final TranslationRepository translationRepository;


    public TranslationService(
            @Qualifier(QualifierConstants.DICTIONARY_QUALIFIER) Retrofit dictionaryRetrofit,
            @Qualifier(QualifierConstants.TRANSLATION_QUALIFIER) Retrofit translationRetrofit,
            AudioService audioService, LocaleRepository localeRepository,
            TranslationRepository translationRepository
    ) {
        this.audioService = audioService;
        this.translationRepository = translationRepository;
        this.localeRepository = localeRepository;
        this.dictionaryRetrofit = dictionaryRetrofit;
        this.translationRetrofit = translationRetrofit;
    }

    public TranslationDto getTranslation(ru.kpfu.itis.gnt.languagelearningapp.dto.TranslationDto translationDto) {
        System.out.println(translationDto);
        TranslationDto model = getTranslationModel(translationDto);
        model.setPresent(checkIfDictionaryExists(translationDto));
        return model;
    }

    private boolean checkIfDictionaryExists(ru.kpfu.itis.gnt.languagelearningapp.dto.TranslationDto translationDto) {
        try {
            YandexDictionaryResponse dictionaryApi = dictionaryRetrofit.create(DictionaryApi.class).getTranslation(
                    translationDto.getFrom().getLanguage() + "-" + translationDto.getTo().getLanguage(), translationDto.getText()
            ).execute().body();
            return dictionaryApi != null && dictionaryApi.getDef().size() > 0;
        } catch (Exception exception) {
            return false;
        }
    }

    private TranslationDto getTranslationModel(ru.kpfu.itis.gnt.languagelearningapp.dto.TranslationDto translationDto) {
        saveLocale(translationDto.getFrom());
        saveLocale(translationDto.getTo());
        Optional<TranslationEntity> translationEntity = translationRepository.findByOriginalIgnoreCase(translationDto.getText());
        TranslationDto model = new TranslationDto();
        if (translationEntity.isEmpty()) {
            try {
                TranslationApi translationApi = translationRetrofit.create(TranslationApi.class);
                TranslationRequest translationRequest = TranslationRequest.builder()
                        .from(translationDto.getFrom().getLanguage())
                        .to(translationDto.getTo().getLanguage())
                        .data(translationDto.getText())
                        .build();
                TranslationResponse translationResponse = translationApi.getTranslation(translationRequest).execute().body();

                if (translationResponse != null) {
                    model.setResult(translationResponse.getResult());
                }
                model.setAudio(audioService.loadAudioRemote(translationDto, model.getResult()));
                translationRepository.findByOriginalIgnoreCase(translationDto.getText()).orElse(
                        translationRepository.save(TranslationEntity.builder()
                                .translation(model.getResult()).original(translationDto.getText()).audio(model.getAudio()).build())
                );
                return model;
            } catch (Exception exception) {
                throw new ResourceNotFoundException(ErrorMessageConstants.NOT_FOUND.TRANSLATION);
            }
        } else {
            model.setResult(translationEntity.get().getTranslation());
            model.setAudio(translationEntity.get().getAudio());
            return model;
        }
    }

    private void saveLocale(Locale locale) {
        if (localeRepository.findByTextIgnoreCase(locale.toString()).isEmpty()) {
            localeRepository.save(LocaleEntity.builder()
                    .text(locale.toString())
                    .build());
        }
    }
}
