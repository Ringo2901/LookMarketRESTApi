package by.bsuir.lookmanager.controllers;

import by.bsuir.lookmanager.dto.ApplicationResponseDto;
import by.bsuir.lookmanager.dto.configuration.ConfigurationResponseDto;
import by.bsuir.lookmanager.services.ConfigurationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/configuration")
public class ConfigurationController {
    @Autowired
    private ConfigurationService configurationService;
    @GetMapping()
    public ResponseEntity<ApplicationResponseDto<ConfigurationResponseDto>> getAllConfiguration(){
        ApplicationResponseDto<ConfigurationResponseDto> responseDto = configurationService.getConfiguration();
        return ResponseEntity.status(responseDto.getCode()).body(responseDto);
    }
}
