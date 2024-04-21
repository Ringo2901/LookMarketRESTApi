package by.bsuir.lookmanager.controllers;

import by.bsuir.lookmanager.RestApplication;
import by.bsuir.lookmanager.dto.ApplicationResponseDto;
import by.bsuir.lookmanager.dto.product.general.GeneralProductResponseDto;
import by.bsuir.lookmanager.dto.user.AssessmentRequestDto;
import by.bsuir.lookmanager.dto.user.AssessmentResponseDto;
import by.bsuir.lookmanager.services.AssessmentService;
import by.bsuir.lookmanager.services.FavoritesService;
import by.bsuir.lookmanager.utils.JwtValidator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/assessments")
@CrossOrigin(origins = "https://ringo2901.github.io")
public class AssessmentController {
    @Autowired
    private AssessmentService assessmentService;
    @Autowired
    private JwtValidator jwtValidator;
    private static final Logger LOGGER = LogManager.getLogger(AssessmentController.class);
    @GetMapping("/{id}")
    public ResponseEntity<ApplicationResponseDto<List<AssessmentResponseDto>>> getAssessmentsBySellerId(@PathVariable Long id){
        LOGGER.info("Start getting assessments with seller id = " + id);
        ApplicationResponseDto<List<AssessmentResponseDto>> responseDto = assessmentService.getAssessmentsBySellerId(id);
        LOGGER.info("Finish getting assessments with seller id = " + id);
        return ResponseEntity.status(responseDto.getCode()).body(responseDto);
    }

    @PostMapping()
    public ResponseEntity<ApplicationResponseDto<?>> addFavorites(@RequestHeader("Authorization") String token, @RequestBody AssessmentRequestDto requestDto){
        LOGGER.info("Start add assessment with request = " + requestDto);
        ApplicationResponseDto<?> responseDto = assessmentService.addAssessment(jwtValidator.validateTokenAndGetUserId(token), requestDto);
        LOGGER.info("Finish add assessment with response = " + responseDto);
        return ResponseEntity.status(responseDto.getCode()).body(responseDto);
    }

    @DeleteMapping("/{sellerId}")
    public ResponseEntity<ApplicationResponseDto<?>> deleteFavorites(@RequestHeader("Authorization") String token, @PathVariable Long sellerId){
        LOGGER.info("Start delete assessment with seller id = " + sellerId);
        ApplicationResponseDto<?> responseDto = assessmentService.removeAssessment(jwtValidator.validateTokenAndGetUserId(token), sellerId);
        LOGGER.info("Start delete assessment with response = " + responseDto);
        return ResponseEntity.status(responseDto.getCode()).body(responseDto);
    }
}
