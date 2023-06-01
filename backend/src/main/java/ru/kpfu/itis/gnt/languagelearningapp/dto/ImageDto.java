package ru.kpfu.itis.gnt.languagelearningapp.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;
import java.util.Objects;

@Schema(description = "Список картинок")
public class ImageDto {
    @Schema(description = "Картинки")
    private List<String> images;


    public List<String> getImages() {
        return images;
    }

    public ImageDto(List<String> images) {
        this.images = images;
    }

    @Override
    public String toString() {
        return "ImageDto{" +
                "images=" + images +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ImageDto imageDto = (ImageDto) o;
        return Objects.equals(images, imageDto.images);
    }

    @Override
    public int hashCode() {
        return Objects.hash(images);
    }

    public void setImages(List<String> images) {
        this.images = images;
    }
}
