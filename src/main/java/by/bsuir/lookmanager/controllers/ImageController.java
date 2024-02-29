package by.bsuir.lookmanager.controllers;

import by.bsuir.lookmanager.dto.ApplicationResponseDto;
import by.bsuir.lookmanager.dto.product.media.ImageDataDto;
import by.bsuir.lookmanager.services.ImageDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/image")
public class ImageController {
    @Autowired
    ImageDataService imageDataService;

    @GetMapping()
    public ApplicationResponseDto<List<ImageDataDto>> getImageDataById(@RequestParam List<Long> ids) {
        return imageDataService.getImageDataById(ids);
    }
}
