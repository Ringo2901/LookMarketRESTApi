package by.bsuir.lookmanager.controllers;

import by.bsuir.lookmanager.dto.ApplicationResponseDto;
import by.bsuir.lookmanager.dto.product.general.GeneralProductResponseDto;
import by.bsuir.lookmanager.services.FavoritesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/favorites")
public class FavoritesController {
    @Autowired
    private FavoritesService favoritesService;
    @GetMapping("/{id}")
    public ResponseEntity<ApplicationResponseDto<List<GeneralProductResponseDto>>> getFavoritesByUserId(@PathVariable Long id){
        ApplicationResponseDto<List<GeneralProductResponseDto>> responseDto = favoritesService.getFavoritesByUserId(id);
        return ResponseEntity.status(responseDto.getCode()).body(responseDto);
    }

    @PostMapping("/{userId}/{productId}")
    public ResponseEntity<ApplicationResponseDto<?>> addFavorites(@PathVariable Long userId, @PathVariable Long productId){
        ApplicationResponseDto<?> responseDto = favoritesService.addFavorite(userId, productId);
        return ResponseEntity.status(responseDto.getCode()).body(responseDto);
    }

    @DeleteMapping("/{userId}/{productId}")
    public ResponseEntity<ApplicationResponseDto<?>> deleteFavorites(@PathVariable Long userId, @PathVariable Long productId){
        ApplicationResponseDto<?> responseDto = favoritesService.deleteFavorite(userId, productId);
        return ResponseEntity.status(responseDto.getCode()).body(responseDto);
    }
}
