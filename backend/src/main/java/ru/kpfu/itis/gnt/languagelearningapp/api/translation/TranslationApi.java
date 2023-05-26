package ru.kpfu.itis.gnt.languagelearningapp.api.translation;

import lombok.val;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import ru.kpfu.itis.gnt.languagelearningapp.api.translation.models.TranslationRequest;
import ru.kpfu.itis.gnt.languagelearningapp.api.translation.models.TranslationResponse;
import ru.kpfu.itis.gnt.languagelearningapp.constants.ExternalApiUrls;

public interface TranslationApi {

    @Headers({
            "content-type: application/json",
            "accept: application/json",
            "Authorization: " + ExternalApiUrls.Translation.API_KEY_VALUE
    })
    @POST("translate")
    Call<TranslationResponse> getTranslation(@Body TranslationRequest request);
}
