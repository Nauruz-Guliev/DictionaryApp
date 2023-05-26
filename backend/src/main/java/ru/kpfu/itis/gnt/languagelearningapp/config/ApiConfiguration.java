package ru.kpfu.itis.gnt.languagelearningapp.config;

import okhttp3.*;
import okhttp3.logging.HttpLoggingInterceptor;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import ru.kpfu.itis.gnt.languagelearningapp.constants.QualifierConstants;
import ru.kpfu.itis.gnt.languagelearningapp.constants.ExternalApiUrls;

import java.util.List;

@Configuration
public class ApiConfiguration {

    private static final HttpLoggingInterceptor okHttpLoggingInterceptor
            = new HttpLoggingInterceptor()
            .setLevel(HttpLoggingInterceptor.Level.BASIC);

    @Bean
    public OkHttpClient client() {
        return new OkHttpClient.Builder()
                .addInterceptor(okHttpLoggingInterceptor)
                .build();
    }

    @Bean
    @Qualifier(QualifierConstants.DICTIONARY_QUALIFIER)
    public Retrofit yandexDictionaryRetrofit(
            GsonConverterFactory factory
    ) {
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(okHttpLoggingInterceptor)
                .addInterceptor(createQueryInterceptor(List.of(
                        new ImmutablePair<>(ExternalApiUrls.Yandex.API_KEY, ExternalApiUrls.Yandex.API_KEY_VALUE)
                )))
                .build();
        return new Retrofit.Builder()
                .baseUrl(ExternalApiUrls.Yandex.URL)
                .addConverterFactory(factory)
                .client(client)
                .build();
    }

    @Bean
    @Qualifier(QualifierConstants.IMAGE_QUALIFIER)
    public Retrofit imageUnsplashRetrofit(
            GsonConverterFactory factory
    ) {
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(okHttpLoggingInterceptor)
                .addInterceptor(createQueryInterceptor(List.of(
                        new ImmutablePair<>(ExternalApiUrls.Unsplash.API_KEY,
                                ExternalApiUrls.Unsplash.API_KEY_VALUE)
                )))
                .build();
        return new Retrofit.Builder()
                .baseUrl(ExternalApiUrls.Unsplash.URL)
                .addConverterFactory(factory)
                .client(client)
                .build();
    }

    @Bean
    @Qualifier(QualifierConstants.SPEECH_QUALIFIER)
    public Retrofit speechVoicerssRetrofit(
            GsonConverterFactory factory
    ) {
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(okHttpLoggingInterceptor)
                .addInterceptor(createQueryInterceptor(List.of(
                        new ImmutablePair<>(ExternalApiUrls.Voicerss.API_KEY,
                                ExternalApiUrls.Voicerss.API_KEY_VALUE),
                        new ImmutablePair<>("c", "MP3"),
                        new ImmutablePair<>("f", "16khz_16bit_stereo")
                )))
                .build();
        return new Retrofit.Builder()
                .baseUrl(ExternalApiUrls.Voicerss.URL)
                .addConverterFactory(factory)
                .client(client)
                .build();
    }

    @Bean
    @Qualifier(QualifierConstants.TRANSLATION_QUALIFIER)
    public Retrofit translationRetrofit(
            OkHttpClient client,
            GsonConverterFactory factory
    ) {
        return new Retrofit.Builder()
                .baseUrl(ExternalApiUrls.Translation.URL)
                .addConverterFactory(factory)
                .client(client)
                .build();
    }

    private Interceptor createQueryInterceptor(List<Pair<String, String>> queries) {
        return chain -> {
            Request originalRequest = chain.request();
            HttpUrl originalUrl = originalRequest.url();
            HttpUrl.Builder modifiedUrl = originalUrl.newBuilder();
            for (Pair<String, String> pair : queries) {
                modifiedUrl.addQueryParameter(pair.getLeft(), pair.getRight());
            }
            Request modifiedRequest = originalRequest.newBuilder()
                    .url(modifiedUrl.build())
                    .build();
            return chain.proceed(modifiedRequest);
        };
    }


    @Bean
    public GsonConverterFactory gsonConverterFactory() {
        return GsonConverterFactory.create();
    }


}
