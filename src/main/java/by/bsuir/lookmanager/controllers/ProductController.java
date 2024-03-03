package by.bsuir.lookmanager.controllers;

import by.bsuir.lookmanager.dto.ApplicationResponseDto;
import by.bsuir.lookmanager.dto.product.details.ProductDetailsRequestDto;
import by.bsuir.lookmanager.dto.product.details.ProductDetailsResponseDto;
import by.bsuir.lookmanager.dto.product.details.ProductInformationRequestDto;
import by.bsuir.lookmanager.dto.product.general.GeneralProductResponseDto;
import by.bsuir.lookmanager.dto.user.UserLoginRequestDto;
import by.bsuir.lookmanager.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/product")
public class ProductController {
    @Autowired
    ProductService productService;

    @GetMapping("/{id}")
    public ResponseEntity<ApplicationResponseDto<ProductDetailsResponseDto>> getProductById(@PathVariable Long id) {
        ApplicationResponseDto<ProductDetailsResponseDto> responseDto = productService.getProductInformationById(id);
        return ResponseEntity.status(responseDto.getCode()).body(responseDto);
    }

    @GetMapping()
    public ResponseEntity<ApplicationResponseDto<List<GeneralProductResponseDto>>> getProducts(@RequestParam(required = false, defaultValue = "0") Integer pageNumber,
                                                                               @RequestParam(required = false, defaultValue = "10") Integer pageSize,
                                                                               @RequestParam(required = false, defaultValue = "id") String sortBy,
                                                                               @RequestParam(required = false, defaultValue = "desc") String sortOrder) {
        ApplicationResponseDto<List<GeneralProductResponseDto>> responseDto = productService.getProducts(pageNumber, pageSize, sortBy, sortOrder);
        return ResponseEntity.status(responseDto.getCode()).body(responseDto);
    }

    @GetMapping("/by-category/{id}")
    public ResponseEntity<ApplicationResponseDto<List<GeneralProductResponseDto>>> getProductsByCategory(@PathVariable Long id,
                                                                                        @RequestParam(required = false, defaultValue = "0") Integer pageNumber,
                                                                                        @RequestParam(required = false, defaultValue = "10") Integer pageSize,
                                                                                        @RequestParam(required = false, defaultValue = "id") String sortBy,
                                                                                        @RequestParam(required = false, defaultValue = "desc") String sortOrder) {
        ApplicationResponseDto<List<GeneralProductResponseDto>> responseDto = productService.getProductsByCategory(id, pageNumber, pageSize, sortBy, sortOrder);
        return ResponseEntity.status(responseDto.getCode()).body(responseDto);
    }

    @PostMapping
    public ResponseEntity<ApplicationResponseDto<ProductDetailsResponseDto>> saveProduct(@RequestBody ProductDetailsRequestDto requestDto) {
        ApplicationResponseDto<ProductDetailsResponseDto> responseDto = productService.saveProduct(requestDto);
        return ResponseEntity.status(responseDto.getCode()).body(responseDto);
    }
    @PutMapping("/{id}")
    public ResponseEntity<ApplicationResponseDto<ProductDetailsResponseDto>> updateProductInformation(@PathVariable Long id, @RequestBody ProductInformationRequestDto requestDto) {
        ApplicationResponseDto<ProductDetailsResponseDto> responseDto = productService.updateProduct(id, requestDto);
        return ResponseEntity.status(responseDto.getCode()).body(responseDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApplicationResponseDto<Object>> deleteProduct(@PathVariable Long id) {
        ApplicationResponseDto<Object> responseDto = productService.deleteProduct(id);
        return ResponseEntity.status(responseDto.getCode()).body(responseDto);
    }

}
