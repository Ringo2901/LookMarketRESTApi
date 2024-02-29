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
    @GetMapping()
    public ApplicationResponseDto<List<ImageDataDto>> getImageDataById(@RequestParam List<Long> ids) {
        return imageDataService.getImageDataById(ids);
    }

    @GetMapping("/{id}")
    public ApplicationResponseDto<ImageDataDto> getUserImageDataByUserId(@PathVariable Long id) {
        return userService.findProfileImageByUserId(id);
    }
}
