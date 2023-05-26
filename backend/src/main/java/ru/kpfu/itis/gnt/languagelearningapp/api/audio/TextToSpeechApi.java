package ru.kpfu.itis.gnt.languagelearningapp.api.audio;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import ru.kpfu.itis.gnt.languagelearningapp.api.image.models.ImageResponse;

public interface TextToSpeechApi {
    @GET(".")
    Call<ResponseBody> getAudio(
            @Query("hl") String locale,
            @Query("src") String text);

}
