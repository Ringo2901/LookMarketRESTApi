package by.bsuir.lookmanager.controllers;

import by.bsuir.lookmanager.dto.ApplicationResponseDto;
import by.bsuir.lookmanager.dto.product.media.ImageDataRequestDto;
import by.bsuir.lookmanager.dto.product.media.ImageDataResponseDto;
import by.bsuir.lookmanager.services.ImageDataService;
import by.bsuir.lookmanager.services.impl.UserServiceImpl;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/image")
@CrossOrigin(origins = "https://ringo2901.github.io")
public class ImageController {
    @Autowired
    private ImageDataService imageDataService;
    private static final Logger LOGGER = LogManager.getLogger(ImageController.class);
    @GetMapping("/byId/{id}")
    public ResponseEntity<ApplicationResponseDto<ImageDataResponseDto>> getImageDataById(@PathVariable Long id) {
        LOGGER.info("Start getting image data by image id = " + id);
        ApplicationResponseDto<ImageDataResponseDto> responseDto = imageDataService.getImageDataById(id);
        LOGGER.info("Finish getting image data by image id = " + id);
        return ResponseEntity.status(responseDto.getCode()).body(responseDto);
    }
    @GetMapping("/{id}")
    public ResponseEntity<ApplicationResponseDto<List<ImageDataResponseDto>>> getImageDataByProductId(@PathVariable Long id) {
        LOGGER.info("Start getting images data by product id = " + id);
        ApplicationResponseDto<List<ImageDataResponseDto>> responseDto = imageDataService.getImageDataByProductId(id);
        LOGGER.info("Finish getting images data by product id = " + id);
        return ResponseEntity.status(responseDto.getCode()).body(responseDto);
    }
    @PostMapping("/{id}")
    public ResponseEntity< ApplicationResponseDto<Object>> addImageDataByProductId(@PathVariable Long id, @RequestBody ImageDataRequestDto requestDto) {
        LOGGER.info("Start adding images data by product id = " + id);
        ApplicationResponseDto<Object> responseDto = imageDataService.addImageDataByProductId(id, requestDto);
        LOGGER.info("Finish adding images data by product id = " + id);
        return ResponseEntity.status(responseDto.getCode()).body(responseDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApplicationResponseDto<Object>> deleteImageDataById(@PathVariable Long id) {
        LOGGER.info("Start deleting images data by image id = " + id);
        ApplicationResponseDto<Object> responseDto = imageDataService.deleteImageDataById(id);
        LOGGER.info("Finish deleting images data by image id = " + id);
        return ResponseEntity.status(responseDto.getCode()).body(responseDto);
    }
}
