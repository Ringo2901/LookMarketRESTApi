package by.bsuir.lookmanager.controllers;

import by.bsuir.lookmanager.dto.ApplicationResponseDto;
import by.bsuir.lookmanager.dto.product.general.GeneralProductResponseDto;
import by.bsuir.lookmanager.services.RecommendedService;
import by.bsuir.lookmanager.utils.JwtValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/recommended")
@CrossOrigin(origins = "https://ringo2901.github.io")
public class RecommendedController {
    @Autowired
    private RecommendedService recommendedService;
    @Autowired
    private JwtValidator jwtValidator;

    @GetMapping()
    public ApplicationResponseDto<List<GeneralProductResponseDto>> getRecommendedProducts(@RequestHeader(value = "Authorization", required = false) Optional<String> token,
                                                                                          @RequestParam Integer pageNumber,
                                                                                          @RequestParam Integer pageSize) {
        return recommendedService.findRecommendedProducts(jwtValidator.validateTokenAndGetUserId(token.orElse(null)), pageNumber, pageSize);
    }
}
