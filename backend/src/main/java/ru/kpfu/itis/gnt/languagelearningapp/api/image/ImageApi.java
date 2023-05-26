package ru.kpfu.itis.gnt.languagelearningapp.api.image;


import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import ru.kpfu.itis.gnt.languagelearningapp.api.image.models.ImageResponse;

public interface ImageApi {
    @GET("search/photos")
    Call<ImageResponse> getImageResponse(
            @Query("query") String image);
}
