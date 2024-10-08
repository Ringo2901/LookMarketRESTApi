package by.bsuir.lookmanager.controllers;

import by.bsuir.lookmanager.dto.ApplicationResponseDto;
import by.bsuir.lookmanager.dto.ListResponseDto;
import by.bsuir.lookmanager.dto.product.details.ProductDetailsRequestDto;
import by.bsuir.lookmanager.dto.product.details.ProductDetailsResponseDto;
import by.bsuir.lookmanager.dto.product.details.ProductInformationRequestDto;
import by.bsuir.lookmanager.dto.product.general.GeneralProductResponseDto;
import by.bsuir.lookmanager.services.ProductService;
import by.bsuir.lookmanager.utils.JwtValidator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/product")
@CrossOrigin(origins = "https://ringo2901.github.io")
public class ProductController {
    @Autowired
    private ProductService productService;
    @Autowired
    private JwtValidator jwtValidator;
    private static final Logger LOGGER = LogManager.getLogger(ProductController.class);

    @GetMapping("/{id}")
    public ResponseEntity<ApplicationResponseDto<ProductDetailsResponseDto>> getProductById(@RequestHeader(value = "Authorization", required = false) Optional<String> token,
                                                                                            @RequestParam(required = false, defaultValue = "en") String lang,
                                                                                            @PathVariable Long id) {
        LOGGER.info("Start getting product info by product id = " + id);
        ApplicationResponseDto<ProductDetailsResponseDto> responseDto = productService.getProductInformationById(jwtValidator.validateTokenAndGetUserId(token.orElse(null)), id, lang);
        LOGGER.info("Finish getting product info by product id = " + id);
        return ResponseEntity.status(responseDto.getCode()).body(responseDto);
    }

    @GetMapping()
    public ResponseEntity<ApplicationResponseDto<ListResponseDto<GeneralProductResponseDto>>> getProducts(@RequestHeader(value = "Authorization", required = false) Optional<String> token, @RequestParam(required = false, defaultValue = "0") Integer pageNumber,
                                                                              @RequestParam(required = false, defaultValue = "10") Integer pageSize,
                                                                              @RequestParam(required = false, defaultValue = "createdTime") String sortBy,
                                                                              @RequestParam(required = false, defaultValue = "desc") String sortOrder) throws SQLException {
        LOGGER.info("Start getting products");
        ApplicationResponseDto<ListResponseDto<GeneralProductResponseDto>> responseDto = productService.getProducts(jwtValidator.validateTokenAndGetUserId(token.orElse(null)), pageNumber, pageSize, sortBy, sortOrder);
        LOGGER.info("Finish getting products");
        return ResponseEntity.status(responseDto.getCode()).body(responseDto);
    }

    @GetMapping("/by-category")
    public ResponseEntity<ApplicationResponseDto<ListResponseDto<GeneralProductResponseDto>>> getProductsByGender(@RequestHeader(value = "Authorization", required = false) Optional<String> token,
                                                                                                       @RequestParam(defaultValue = "MALE") String sex,
                                                                                                       @RequestParam(required = false, defaultValue = "0") Integer pageNumber,
                                                                                                       @RequestParam(required = false, defaultValue = "10") Integer pageSize,
                                                                                                       @RequestParam(required = false, defaultValue = "createdTime") String sortBy,
                                                                                                       @RequestParam(required = false, defaultValue = "desc") String sortOrder) throws SQLException {
        LOGGER.info("Start getting products by gender = " + sex);
        ApplicationResponseDto<ListResponseDto<GeneralProductResponseDto>> responseDto = productService.getProductsByCategory(jwtValidator.validateTokenAndGetUserId(token.orElse(null)), sex, pageNumber, pageSize, sortBy, sortOrder);
        LOGGER.info("Finish getting products by gender = " + sex);
        return ResponseEntity.status(responseDto.getCode()).body(responseDto);
    }

    @GetMapping("/sorting")
    public ResponseEntity<ApplicationResponseDto<ListResponseDto<GeneralProductResponseDto>>> getProductsWithSorting(@RequestHeader(value = "Authorization", required = false) Optional<String> token, @RequestParam(required = false) String query,
                                                                                                          @RequestParam(required = false, defaultValue = "10") Integer pageSize,
                                                                                                          @RequestParam(required = false, defaultValue = "0") Integer pageNumber,
                                                                                                          @RequestParam(required = false, defaultValue = "createdTime") String sortBy,
                                                                                                          @RequestParam(required = false, defaultValue = "desc") String sortOrder,
                                                                                                          @RequestParam(required = false) List<Integer> sizes,
                                                                                                          @RequestParam(required = false) List<Integer> colors,
                                                                                                          @RequestParam(required = false) List<Integer> brand,
                                                                                                          @RequestParam(required = false) List<Integer> seasons,
                                                                                                          @RequestParam(required = false) List<Integer> genders,
                                                                                                          @RequestParam(required = false) List<Integer> ageTypes,
                                                                                                          @RequestParam(required = false) List<Integer> tags,
                                                                                                          @RequestParam(required = false) List<Integer> materials,
                                                                                                          @RequestParam(required = false) List<Integer> subcategory,
                                                                                                          @RequestParam(required = false) List<Integer> category,
                                                                                                          @RequestParam(required = false, defaultValue = "0") Double minPrice,
                                                                                                          @RequestParam(required = false, defaultValue = "1000") Double maxPrice) throws SQLException {
        LOGGER.info("Start getting products with sorting and filtering");
        ApplicationResponseDto<ListResponseDto<GeneralProductResponseDto>> responseDto = productService.getProductsWithSorting(jwtValidator.validateTokenAndGetUserId(token.orElse(null)), query, pageSize, ++pageNumber, sortBy, sortOrder, sizes, colors, brand, seasons, genders, ageTypes, tags, materials, subcategory, category, minPrice, maxPrice);
        LOGGER.info("Finish getting products with sorting and filtering");
        return ResponseEntity.status(responseDto.getCode()).body(responseDto);
    }

    @PostMapping
    public ResponseEntity<ApplicationResponseDto<Long>> saveProduct(@RequestBody ProductDetailsRequestDto requestDto) {
        LOGGER.info("Start adding product with request = " + requestDto);
        ApplicationResponseDto<Long> responseDto = productService.saveProduct(requestDto);
        LOGGER.info("Finish adding product with response = " + responseDto);
        return ResponseEntity.status(responseDto.getCode()).body(responseDto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApplicationResponseDto<ProductDetailsResponseDto>> updateProductInformation(@RequestHeader(value = "Authorization", required = false) Optional<String> token, @PathVariable Long id, @RequestBody ProductInformationRequestDto requestDto) {
        LOGGER.info("Start updating product with product id = " + id + "request = " + requestDto);
        ApplicationResponseDto<ProductDetailsResponseDto> responseDto = productService.updateProduct(jwtValidator.validateTokenAndGetUserId(token.orElse(null)), id, requestDto);
        LOGGER.info("Finish updating product with response = " + responseDto);
        return ResponseEntity.status(responseDto.getCode()).body(responseDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApplicationResponseDto<Object>> deleteProduct(@PathVariable Long id) {
        LOGGER.info("Start deleting product with product id = " + id);
        ApplicationResponseDto<Object> responseDto = productService.deleteProduct(id);
        LOGGER.info("Finish deleting product with product id = " + id);
        return ResponseEntity.status(responseDto.getCode()).body(responseDto);
    }

}
