package by.bsuir.lookmanager.controllers;

import by.bsuir.lookmanager.dto.ApplicationResponseDto;
import by.bsuir.lookmanager.dto.configuration.ConfigurationResponseDto;
import by.bsuir.lookmanager.services.ConfigurationService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/configuration")
@CrossOrigin(origins = "https://ringo2901.github.io")
public class ConfigurationController {
    @Autowired
    private ConfigurationService configurationService;
    private static final Logger LOGGER = LogManager.getLogger(ConfigurationController.class);
    @GetMapping()
    public ResponseEntity<ApplicationResponseDto<ConfigurationResponseDto>> getAllConfiguration(@RequestParam(required = false, defaultValue = "en") String lang){
        LOGGER.info("Start getting config");
        ApplicationResponseDto<ConfigurationResponseDto> responseDto = configurationService.getConfiguration(lang);
        LOGGER.info("Finish getting config");
        return ResponseEntity.status(responseDto.getCode()).body(responseDto);
    }
}
