package ru.kpfu.itis.gnt.languagelearningapp.services;

import okhttp3.ResponseBody;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import retrofit2.Response;
import retrofit2.Retrofit;
import ru.kpfu.itis.gnt.languagelearningapp.api.audio.TextToSpeechApi;
import ru.kpfu.itis.gnt.languagelearningapp.constants.QualifierConstants;
import ru.kpfu.itis.gnt.languagelearningapp.dto.TranslationDto;

@Service
public class AudioService {
    private final Retrofit speechRetrofit;

    public AudioService(
            @Qualifier(QualifierConstants.SPEECH_QUALIFIER) Retrofit speechRetrofit) {
        this.speechRetrofit = speechRetrofit;
    }


    public byte[] loadAudioRemote(TranslationDto translationDto, String text) {
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


}
