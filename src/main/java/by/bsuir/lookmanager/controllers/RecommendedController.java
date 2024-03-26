package by.bsuir.lookmanager.controllers;

import by.bsuir.lookmanager.dto.ApplicationResponseDto;
import by.bsuir.lookmanager.dto.product.general.GeneralProductResponseDto;
import by.bsuir.lookmanager.services.RecommendedService;
import by.bsuir.lookmanager.utils.JwtValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/recommended")
public class RecommendedController {
    @Autowired
    private RecommendedService recommendedService;
    @Autowired
    private JwtValidator jwtValidator;
    @GetMapping()
    public ApplicationResponseDto<List<GeneralProductResponseDto>> getRecommendedProducts(@RequestHeader("Authorization") String token,
                                                                               @RequestParam Long numberOfProducts) {
        return recommendedService.findRecommendedProducts(jwtValidator.validateTokenAndGetUserId(token), numberOfProducts);
    }
}
