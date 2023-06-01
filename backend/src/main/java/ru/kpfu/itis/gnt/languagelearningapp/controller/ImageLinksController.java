package ru.kpfu.itis.gnt.languagelearningapp.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.tags.Tags;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import ru.kpfu.itis.gnt.languagelearningapp.dto.ImageDto;
import ru.kpfu.itis.gnt.languagelearningapp.services.ImageService;

@RequestMapping("api/v1/")
@Tags(value = {
        @Tag(name = "Ссылки картинок")
})
@RestController
public class ImageLinksController {
    private final ImageService service;

    @Autowired
    private ImageLinksController(ImageService service){
        this.service = service;
    }

    @Operation(summary = "Картинки")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Список",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ImageDto.class))
                    }
            )
    })
    @RequestMapping(value = "image/{image}", method = RequestMethod.GET)
    public ImageDto getImage(@PathVariable(value = "image") String name) {
        return new ImageDto(service.getImages(name));
    }

}
