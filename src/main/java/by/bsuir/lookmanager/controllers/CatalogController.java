package by.bsuir.lookmanager.controllers;

import by.bsuir.lookmanager.dto.ApplicationResponseDto;
import by.bsuir.lookmanager.dto.catalog.CatalogRequestDto;
import by.bsuir.lookmanager.dto.catalog.CatalogResponseDto;
import by.bsuir.lookmanager.dto.catalog.CatalogWithItemsDto;
import by.bsuir.lookmanager.services.CatalogService;
import by.bsuir.lookmanager.utils.JwtValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/catalogs")
@CrossOrigin(origins = "https://ringo2901.github.io")
public class CatalogController {
    @Autowired
    private CatalogService catalogService;
    @Autowired
    private JwtValidator jwtValidator;

    @GetMapping("/{id}")
    public ResponseEntity<ApplicationResponseDto<CatalogWithItemsDto>> getCatalogInfoById(@PathVariable Long id) {
        ApplicationResponseDto<CatalogWithItemsDto> responseDto = catalogService.getCatalogInfoByCatalogId(id);
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
