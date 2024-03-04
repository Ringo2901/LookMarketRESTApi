package by.bsuir.lookmanager.controllers;

import by.bsuir.lookmanager.dto.ApplicationResponseDto;
import by.bsuir.lookmanager.dto.product.general.GeneralProductResponseDto;
import by.bsuir.lookmanager.services.RecommendedService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/recommended")
public class RecommendedController {
    @Autowired
    private RecommendedService recommendedService;
    @GetMapping()
    public ApplicationResponseDto<List<GeneralProductResponseDto>> getRecommendedProducts(@RequestParam Long userId,
                                                                               @RequestParam Long numberOfProducts) {
        return recommendedService.findRecommendedProducts(userId, numberOfProducts);
    }
}
