package by.bsuir.lookmanager.controllers;

import by.bsuir.lookmanager.dto.ApplicationResponseDto;
import by.bsuir.lookmanager.dto.product.media.ImageDataRequestDto;
import by.bsuir.lookmanager.dto.product.media.ImageDataResponseDto;
import by.bsuir.lookmanager.services.ImageDataService;
import by.bsuir.lookmanager.services.impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/image")
public class ImageController {
    @Autowired
    private ImageDataService imageDataService;
    @GetMapping("/{id}")
    public ResponseEntity<ApplicationResponseDto<List<ImageDataResponseDto>>> getImageDataByProductId(@PathVariable Long id) {
        ApplicationResponseDto<List<ImageDataResponseDto>> responseDto = imageDataService.getImageDataByProductId(id);
        return ResponseEntity.status(responseDto.getCode()).body(responseDto);
    }
    @PostMapping("/{id}")
    public ResponseEntity<ApplicationResponseDto<List<ImageDataResponseDto>>> addImageDataByProductId(@PathVariable Long id, @RequestBody ImageDataRequestDto requestDto) {
        ApplicationResponseDto<List<ImageDataResponseDto>> responseDto = imageDataService.addImageDataByProductId(id, requestDto);
        return ResponseEntity.status(responseDto.getCode()).body(responseDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApplicationResponseDto<Object>> deleteImageDataById(@PathVariable Long id) {
        ApplicationResponseDto<Object> responseDto = imageDataService.deleteImageDataById(id);
        return ResponseEntity.status(responseDto.getCode()).body(responseDto);
    }
}
