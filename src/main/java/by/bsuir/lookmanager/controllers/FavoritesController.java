package by.bsuir.lookmanager.controllers;

import by.bsuir.lookmanager.dto.ApplicationResponseDto;
import by.bsuir.lookmanager.dto.product.general.GeneralProductResponseDto;
import by.bsuir.lookmanager.services.FavoritesService;
import by.bsuir.lookmanager.utils.JwtValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/favorites")
public class FavoritesController {
    @Autowired
    private FavoritesService favoritesService;
    @Autowired
    private JwtValidator jwtValidator;
    @GetMapping()
    public ResponseEntity<ApplicationResponseDto<List<GeneralProductResponseDto>>> getFavoritesByUserId(@RequestHeader("Authorization") String token){
        ApplicationResponseDto<List<GeneralProductResponseDto>> responseDto = favoritesService.getFavoritesByUserId(jwtValidator.validateTokenAndGetUserId(token));
        return ResponseEntity.status(responseDto.getCode()).body(responseDto);
    }

    @PostMapping("/{productId}")
    public ResponseEntity<ApplicationResponseDto<?>> addFavorites(@RequestHeader("Authorization") String token, @PathVariable Long productId){
        ApplicationResponseDto<?> responseDto = favoritesService.addFavorite(jwtValidator.validateTokenAndGetUserId(token), productId);
        return ResponseEntity.status(responseDto.getCode()).body(responseDto);
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<ApplicationResponseDto<?>> deleteFavorites(@RequestHeader("Authorization") String token, @PathVariable Long productId){
        ApplicationResponseDto<?> responseDto = favoritesService.deleteFavorite(jwtValidator.validateTokenAndGetUserId(token), productId);
        return ResponseEntity.status(responseDto.getCode()).body(responseDto);
    }
}
