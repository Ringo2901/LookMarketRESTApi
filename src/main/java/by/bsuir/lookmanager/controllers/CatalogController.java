package by.bsuir.lookmanager.controllers;

import by.bsuir.lookmanager.dto.ApplicationResponseDto;
import by.bsuir.lookmanager.dto.catalog.CatalogRequestDto;
import by.bsuir.lookmanager.dto.catalog.CatalogResponseDto;
import by.bsuir.lookmanager.dto.catalog.CatalogWithItemsDto;
import by.bsuir.lookmanager.dto.product.general.GeneralProductResponseDto;
import by.bsuir.lookmanager.services.CatalogService;
import by.bsuir.lookmanager.utils.JwtValidator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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
    private static final Logger LOGGER = LogManager.getLogger(CatalogController.class);

    @GetMapping("/{id}")
    public ResponseEntity<ApplicationResponseDto<List<GeneralProductResponseDto>>> getCatalogInfoById(@PathVariable Long id,
                                                                                                      @RequestHeader(value = "Authorization", required = false) Optional<String> token,
                                                                                                      @RequestParam(required = false, defaultValue = "0") Integer pageNumber,
                                                                                                      @RequestParam(required = false, defaultValue = "10") Integer pageSize,
                                                                                                      @RequestParam(required = false, defaultValue = "createdTime") String sortBy,
                                                                                                      @RequestParam(required = false, defaultValue = "desc") String sortOrder) {
        LOGGER.info("Start getting catalog info by catalog id = " + id);
        ApplicationResponseDto<List<GeneralProductResponseDto>> responseDto = catalogService.getCatalogInfoByCatalogId(id, jwtValidator.validateTokenAndGetUserId(token.orElse(null)), pageNumber, pageSize, sortBy, sortOrder);
        LOGGER.info("Finish getting catalog info by catalog id = " + id);
        return ResponseEntity.status(responseDto.getCode()).body(responseDto);
    }

    @GetMapping("/products/{id}")
    public ResponseEntity<ApplicationResponseDto<List<CatalogWithItemsDto>>> getCatalogsFirst2ProductsById(@PathVariable Long id) {
        LOGGER.info("Start getting catalogs first 2 items by user id = " + id);
        ApplicationResponseDto<List<CatalogWithItemsDto>> responseDto = catalogService.getCatalogsItemsByUserId(id);
        LOGGER.info("Finish getting catalogs first 2 items by user id = " + id);
        return ResponseEntity.status(responseDto.getCode()).body(responseDto);
    }

    @PostMapping
    public ResponseEntity<ApplicationResponseDto<CatalogResponseDto>> saveProduct(@RequestHeader("Authorization") String token, @RequestBody CatalogRequestDto requestDto) {
        LOGGER.info("Start creating new catalog with request = " + requestDto);
        ApplicationResponseDto<CatalogResponseDto> responseDto = catalogService.addCatalog(jwtValidator.validateTokenAndGetUserId(token), requestDto);
        LOGGER.info("Finish creating new catalog with response = " + responseDto);
        return ResponseEntity.status(responseDto.getCode()).body(responseDto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApplicationResponseDto<CatalogResponseDto>> updateProductInformation(@RequestHeader("Authorization") String token, @PathVariable Long id, @RequestBody CatalogRequestDto requestDto) {
        LOGGER.info("Start update catalog with catalog id = " + id + " and request = " + requestDto);
        ApplicationResponseDto<CatalogResponseDto> responseDto = catalogService.updateCatalog(jwtValidator.validateTokenAndGetUserId(token), id, requestDto);
        LOGGER.info("Finish update catalog with response = " + responseDto);
        return ResponseEntity.status(responseDto.getCode()).body(responseDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApplicationResponseDto<Object>> deleteProduct(@RequestHeader("Authorization") String token, @PathVariable Long id) {
        LOGGER.info("Start deleting catalog with catalog id = " + id);
        ApplicationResponseDto<Object> responseDto = catalogService.removeCatalog(jwtValidator.validateTokenAndGetUserId(token), id);
        LOGGER.info("Finish deleting catalog with response = " + responseDto);
        return ResponseEntity.status(responseDto.getCode()).body(responseDto);
    }
}
