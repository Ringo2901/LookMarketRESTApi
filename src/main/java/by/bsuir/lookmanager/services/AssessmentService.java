package by.bsuir.lookmanager.services;

import by.bsuir.lookmanager.dto.ApplicationResponseDto;
import by.bsuir.lookmanager.dto.user.AssessmentRequestDto;
import by.bsuir.lookmanager.dto.user.AssessmentResponseDto;

import java.util.List;

public interface AssessmentService {
    ApplicationResponseDto<?> addAssessment(Long userId, AssessmentRequestDto requestDto);
    ApplicationResponseDto<?> removeAssessment(Long userId, Long sellerId);
    ApplicationResponseDto<List<AssessmentResponseDto>> getAssessmentsBySellerId(Long sellerId);
}
