package by.bsuir.lookmanager.controllers;

import by.bsuir.lookmanager.dto.ApplicationResponseDto;
import by.bsuir.lookmanager.dto.catalog.CatalogRequestDto;
import by.bsuir.lookmanager.dto.catalog.CatalogResponseDto;
import by.bsuir.lookmanager.dto.catalog.CatalogWithItemsDto;
import by.bsuir.lookmanager.dto.product.general.GeneralProductResponseDto;
import by.bsuir.lookmanager.services.CatalogService;
import by.bsuir.lookmanager.utils.JwtValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/catalogs")
@CrossOrigin(origins = "https://ringo2901.github.io")
public class CatalogController {
    @Autowired
    private CatalogService catalogService;
    @Autowired
    private JwtValidator jwtValidator;

    @GetMapping("/{id}")
    public ResponseEntity<ApplicationResponseDto<List<GeneralProductResponseDto>>> getCatalogInfoById(@PathVariable Long id,
                                                                                                      @RequestHeader(value = "Authorization", required = false) Optional<String> token,
                                                                                                      @RequestParam(required = false, defaultValue = "0") Integer pageNumber,
                                                                                                      @RequestParam(required = false, defaultValue = "10") Integer pageSize,
                                                                                                      @RequestParam(required = false, defaultValue = "createdTime") String sortBy,
                                                                                                      @RequestParam(required = false, defaultValue = "desc") String sortOrder) {
        ApplicationResponseDto<List<GeneralProductResponseDto>> responseDto = catalogService.getCatalogInfoByCatalogId(id, jwtValidator.validateTokenAndGetUserId(token.orElse(null)), pageNumber, pageSize, sortBy, sortOrder);
        return ResponseEntity.status(responseDto.getCode()).body(responseDto);
    }

    @GetMapping("/products/{id}")
    public ResponseEntity<ApplicationResponseDto<List<CatalogWithItemsDto>>> getCatalogsFirst2ProductsById(@PathVariable Long id) {
        ApplicationResponseDto<List<CatalogWithItemsDto>> responseDto = catalogService.getCatalogsItemsByUserId(id);
        return ResponseEntity.status(responseDto.getCode()).body(responseDto);
    }

    @PostMapping
    public ResponseEntity<ApplicationResponseDto<CatalogResponseDto>> saveProduct(@RequestHeader("Authorization") String token, @RequestBody CatalogRequestDto requestDto) {
        ApplicationResponseDto<CatalogResponseDto> responseDto = catalogService.addCatalog(jwtValidator.validateTokenAndGetUserId(token), requestDto);
        return ResponseEntity.status(responseDto.getCode()).body(responseDto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApplicationResponseDto<CatalogResponseDto>> updateProductInformation(@RequestHeader("Authorization") String token, @PathVariable Long id, @RequestBody CatalogRequestDto requestDto) {
        ApplicationResponseDto<CatalogResponseDto> responseDto = catalogService.updateCatalog(jwtValidator.validateTokenAndGetUserId(token), id, requestDto);
        return ResponseEntity.status(responseDto.getCode()).body(responseDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApplicationResponseDto<Object>> deleteProduct(@RequestHeader("Authorization") String token, @PathVariable Long id) {
        ApplicationResponseDto<Object> responseDto = catalogService.removeCatalog(jwtValidator.validateTokenAndGetUserId(token), id);
        return ResponseEntity.status(responseDto.getCode()).body(responseDto);
    }
}
