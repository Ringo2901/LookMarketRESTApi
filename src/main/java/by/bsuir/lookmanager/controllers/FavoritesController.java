package by.bsuir.lookmanager.controllers;

import by.bsuir.lookmanager.dto.ApplicationResponseDto;
import by.bsuir.lookmanager.dto.product.general.GeneralProductResponseDto;
import by.bsuir.lookmanager.services.FavoritesService;
import by.bsuir.lookmanager.utils.JwtValidator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/favorites")
@CrossOrigin(origins = "https://ringo2901.github.io")
public class FavoritesController {
    @Autowired
    private FavoritesService favoritesService;
    @Autowired
    private JwtValidator jwtValidator;
    private static final Logger LOGGER = LogManager.getLogger(ConfigurationController.class);
    @GetMapping()
    public ResponseEntity<ApplicationResponseDto<List<GeneralProductResponseDto>>> getFavoritesByUserId(@RequestHeader("Authorization") String token,
                                                                                                        @RequestParam(required = false, defaultValue = "0") Integer pageNumber,
                                                                                                        @RequestParam(required = false, defaultValue = "10") Integer pageSize){
        LOGGER.info("Start getting favourites by user id = " + jwtValidator.validateTokenAndGetUserId(token));
        ApplicationResponseDto<List<GeneralProductResponseDto>> responseDto = favoritesService.getFavoritesByUserId(jwtValidator.validateTokenAndGetUserId(token), pageNumber, pageSize);
        LOGGER.info("Finish getting favourites by user id = " + jwtValidator.validateTokenAndGetUserId(token));
        return ResponseEntity.status(responseDto.getCode()).body(responseDto);
    }

    @PostMapping("/{productId}")
    public ResponseEntity<ApplicationResponseDto<?>> addFavorites(@RequestHeader("Authorization") String token, @PathVariable Long productId){
        LOGGER.info("Start adding favourite by user id = " + jwtValidator.validateTokenAndGetUserId(token) + "and product id = " + productId);
        ApplicationResponseDto<?> responseDto = favoritesService.addFavorite(jwtValidator.validateTokenAndGetUserId(token), productId);
        LOGGER.info("Finish adding favourite by user id = " + jwtValidator.validateTokenAndGetUserId(token) + "and product id = " + productId);
        return ResponseEntity.status(responseDto.getCode()).body(responseDto);
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<ApplicationResponseDto<?>> deleteFavorites(@RequestHeader("Authorization") String token, @PathVariable Long productId){
        LOGGER.info("Start deleting favourite by user id = " + jwtValidator.validateTokenAndGetUserId(token) + "and product id = " + productId);
        ApplicationResponseDto<?> responseDto = favoritesService.deleteFavorite(jwtValidator.validateTokenAndGetUserId(token), productId);
        LOGGER.info("Finish deleting favourite by user id = " + jwtValidator.validateTokenAndGetUserId(token) + "and product id = " + productId);
        return ResponseEntity.status(responseDto.getCode()).body(responseDto);
    }
}
