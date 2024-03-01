package by.bsuir.lookmanager.controllers;

import by.bsuir.lookmanager.dto.ApplicationResponseDto;
import by.bsuir.lookmanager.dto.product.media.ImageDataDto;
import by.bsuir.lookmanager.services.ImageDataService;
import by.bsuir.lookmanager.services.impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/image")
public class ImageController {
    @Autowired
    ImageDataService imageDataService;
    @Autowired
    UserServiceImpl userService;
    @GetMapping("/{id}")
    public ApplicationResponseDto<List<ImageDataDto>> getImageDataByProductId(@PathVariable Long id) {
        return imageDataService.getImageDataByProductId(id);
    }
    @GetMapping("productList/{id}")
    public ApplicationResponseDto<ImageDataDto> getFirstImageDataByProductId(@PathVariable Long id) {
        return imageDataService.getFirstImageDataByProductId(id);
    }

    @GetMapping("/user/{id}")
    public ApplicationResponseDto<ImageDataDto> getUserImageDataByUserId(@PathVariable Long id) {
        return userService.findProfileImageByUserId(id);
    }
}
