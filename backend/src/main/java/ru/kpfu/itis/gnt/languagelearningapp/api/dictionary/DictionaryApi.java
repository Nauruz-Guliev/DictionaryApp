package ru.kpfu.itis.gnt.languagelearningapp.api.dictionary;


import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import ru.kpfu.itis.gnt.languagelearningapp.api.dictionary.models.YandexDictionaryResponse;

public interface DictionaryApi {
    @GET("api/v1/dicservice.json/lookup")
    Call<YandexDictionaryResponse> getTranslation(
            @Query("lang") String language,
            @Query("text") String text);

}
