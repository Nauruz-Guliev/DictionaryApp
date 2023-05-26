package ru.kpfu.itis.gnt.languagelearningapp.services;


import okhttp3.ResponseBody;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import retrofit2.Response;
import retrofit2.Retrofit;
import ru.kpfu.itis.gnt.languagelearningapp.api.audio.TextToSpeechApi;
import ru.kpfu.itis.gnt.languagelearningapp.api.dictionary.DictionaryApi;
import ru.kpfu.itis.gnt.languagelearningapp.api.dictionary.models.YandexDictionaryResponse;
import ru.kpfu.itis.gnt.languagelearningapp.api.translation.TranslationApi;
import ru.kpfu.itis.gnt.languagelearningapp.api.translation.models.TranslationRequest;
import ru.kpfu.itis.gnt.languagelearningapp.api.translation.models.TranslationResponse;
import ru.kpfu.itis.gnt.languagelearningapp.constants.ErrorMessageConstants;
import ru.kpfu.itis.gnt.languagelearningapp.constants.QualifierConstants;
import ru.kpfu.itis.gnt.languagelearningapp.dto.TranslationDto;
import ru.kpfu.itis.gnt.languagelearningapp.entities.LocaleEntity;
import ru.kpfu.itis.gnt.languagelearningapp.entities.TranslationEntity;
import ru.kpfu.itis.gnt.languagelearningapp.exception.ResourceNotFoundException;
import ru.kpfu.itis.gnt.languagelearningapp.model.TranslationModel;
import ru.kpfu.itis.gnt.languagelearningapp.repository.LocaleRepository;
import ru.kpfu.itis.gnt.languagelearningapp.repository.TranslationRepository;

import java.util.Optional;

@Service
public class TranslationService {
    private final Retrofit speechRetrofit;
    private final Retrofit translationRetrofit;
    private final Retrofit dictionaryRetrofit;
    private final LocaleRepository localeRepository;
    private final TranslationRepository translationRepository;


    public TranslationService(
            @Qualifier(QualifierConstants.DICTIONARY_QUALIFIER) Retrofit dictionaryRetrofit,
            @Qualifier(QualifierConstants.SPEECH_QUALIFIER) Retrofit speechRetrofit,
            @Qualifier(QualifierConstants.TRANSLATION_QUALIFIER) Retrofit translationRetrofit,
            LocaleRepository localeRepository,
            TranslationRepository translationRepository
    ) {
        this.translationRepository = translationRepository;
        this.localeRepository = localeRepository;
        this.dictionaryRetrofit = dictionaryRetrofit;
        this.speechRetrofit = speechRetrofit;
        this.translationRetrofit = translationRetrofit;
    }

    public TranslationModel getTranslation(TranslationDto translationDto) {
        System.out.println(translationDto);
        TranslationModel model = getTranslationModel(translationDto);
        model.setPresent(checkIfDictionaryExists(translationDto));
        return model;
    }

    private boolean checkIfDictionaryExists(TranslationDto translationDto) {
        try {
            YandexDictionaryResponse dictionaryApi = dictionaryRetrofit.create(DictionaryApi.class).getTranslation(
                    translationDto.getFrom().getLanguage() + "-" + translationDto.getTo().getLanguage(), translationDto.getText()
            ).execute().body();
            return dictionaryApi != null && dictionaryApi.getDef().size() > 0;
        } catch (Exception exception) {
            return false;
        }
    }

    private TranslationModel getTranslationModel(TranslationDto translationDto) {
        saveLocale(translationDto);
        Optional<TranslationEntity> translationEntity = translationRepository.findByTextCaseInsensitive(translationDto.getText());
        TranslationModel model = new TranslationModel();
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
                model.setAudio(loadAudioRemote(translationDto, model.getResult()));
                translationRepository.findByTextCaseInsensitive(translationDto.getText()).orElse(
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

    private byte[] loadAudioRemote(TranslationDto translationDto, String text) {
        TextToSpeechApi textToSpeechApi = speechRetrofit.create(TextToSpeechApi.class);
        try {
            Response<ResponseBody> result = textToSpeechApi.getAudio(translationDto.getTo().toString().replace("_", "-"), text).execute();
            if (result.body() != null) {
                return result.body().bytes();
            }
        } catch (Exception ignored) {
        }
        return new byte[0];
    }

    private void saveLocale(TranslationDto translationDto) {
        try {
            localeRepository.save(LocaleEntity.builder()
                    .text(translationDto.getFrom().toString())
                    .build());
            localeRepository.save(LocaleEntity.builder()
                    .text(translationDto.getTo().toString())
                    .build());
        } catch (Exception ignored) {
        }
    }
}
