package by.bsuir.lookmanager.controllers;

import by.bsuir.lookmanager.dto.ApplicationResponseDto;
import by.bsuir.lookmanager.dto.product.general.GeneralProductResponseDto;
import by.bsuir.lookmanager.dto.user.AssessmentRequestDto;
import by.bsuir.lookmanager.dto.user.AssessmentResponseDto;
import by.bsuir.lookmanager.services.AssessmentService;
import by.bsuir.lookmanager.services.FavoritesService;
import by.bsuir.lookmanager.utils.JwtValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/assessments")
public class AssessmentController {
    @Autowired
    private AssessmentService assessmentService;
    @Autowired
    private JwtValidator jwtValidator;
    @GetMapping("/{id}")
    public ResponseEntity<ApplicationResponseDto<List<AssessmentResponseDto>>> getAssessmentsBySellerId(@PathVariable Long id){
        ApplicationResponseDto<List<AssessmentResponseDto>> responseDto = assessmentService.getAssessmentsBySellerId(id);
        return ResponseEntity.status(responseDto.getCode()).body(responseDto);
    }

    @PostMapping()
    public ResponseEntity<ApplicationResponseDto<?>> addFavorites(@RequestHeader("Authorization") String token, @RequestBody AssessmentRequestDto requestDto){
        ApplicationResponseDto<?> responseDto = assessmentService.addAssessment(jwtValidator.validateTokenAndGetUserId(token), requestDto);
        return ResponseEntity.status(responseDto.getCode()).body(responseDto);
    }

    @DeleteMapping("/{sellerId}")
    public ResponseEntity<ApplicationResponseDto<?>> deleteFavorites(@RequestHeader("Authorization") String token, @PathVariable Long sellerId){
        ApplicationResponseDto<?> responseDto = assessmentService.removeAssessment(jwtValidator.validateTokenAndGetUserId(token), sellerId);
        return ResponseEntity.status(responseDto.getCode()).body(responseDto);
    }
}
