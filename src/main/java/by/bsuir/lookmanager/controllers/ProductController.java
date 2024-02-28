package by.bsuir.lookmanager.controllers;

import by.bsuir.lookmanager.dto.ApplicationResponseDto;
import by.bsuir.lookmanager.dto.product.details.ProductDetailsResponseDto;
import by.bsuir.lookmanager.dto.product.general.GeneralProductResponseDto;
import by.bsuir.lookmanager.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/product")
public class ProductController {
    @Autowired
    ProductService productService;

    @GetMapping("/{id}")
    public ApplicationResponseDto<ProductDetailsResponseDto> getProductById(@PathVariable Long id) {
        return productService.getProductInformationById(id);
    }

    @GetMapping()
    public ApplicationResponseDto<List<GeneralProductResponseDto>> getProducts(@RequestParam(required = false, defaultValue = "0") Integer pageNumber,
                                                                               @RequestParam(required = false, defaultValue = "10") Integer pageSize,
                                                                               @RequestParam(required = false, defaultValue = "id") String sortBy,
                                                                               @RequestParam(required = false, defaultValue = "desc") String sortOrder) {
        return productService.getProducts(pageNumber, pageSize, sortBy, sortOrder);
    }

    @GetMapping("/by-category/{id}")
    public ApplicationResponseDto<List<GeneralProductResponseDto>> getProductsByCategory(@PathVariable Long id,
                                                                                        @RequestParam(required = false, defaultValue = "0") Integer pageNumber,
                                                                                        @RequestParam(required = false, defaultValue = "10") Integer pageSize,
                                                                                        @RequestParam(required = false, defaultValue = "id") String sortBy,
                                                                                        @RequestParam(required = false, defaultValue = "desc") String sortOrder) {
        return productService.getProductsByCategory(id, pageNumber, pageSize, sortBy, sortOrder);
    }
}
