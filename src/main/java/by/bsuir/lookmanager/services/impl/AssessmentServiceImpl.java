package by.bsuir.lookmanager.services.impl;

import by.bsuir.lookmanager.dao.AssessmentRepository;
import by.bsuir.lookmanager.dao.UserRepository;
import by.bsuir.lookmanager.dto.ApplicationResponseDto;
import by.bsuir.lookmanager.dto.user.AssessmentRequestDto;
import by.bsuir.lookmanager.dto.user.AssessmentResponseDto;
import by.bsuir.lookmanager.dto.user.mapper.AssessmentMapper;
import by.bsuir.lookmanager.entities.user.information.Assessments;
import by.bsuir.lookmanager.services.AssessmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AssessmentServiceImpl implements AssessmentService {
    @Autowired
    private AssessmentRepository assessmentRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AssessmentMapper assessmentMapper;
    @Override
    public ApplicationResponseDto<?> addAssessment(Long userId, AssessmentRequestDto requestDto) {
        ApplicationResponseDto<?> responseDto = new ApplicationResponseDto<>();
        Assessments assessments = assessmentMapper.requestDtoToAssessmentEntity(requestDto);
        assessments.setUserId(userId);
        assessmentRepository.save(assessments);
        responseDto.setMessage("Assessment add!");
        responseDto.setStatus("OK");
        responseDto.setCode(200);
        return responseDto;
    }

    @Override
    public ApplicationResponseDto<?> removeAssessment(Long userId,Long sellerId) {
        ApplicationResponseDto<?> responseDto = new ApplicationResponseDto<>();
        Assessments assessments = assessmentRepository.findFirstByUserIdAndSellerId(userId, sellerId).orElseThrow(() -> new RuntimeException("exception"));
        assessmentRepository.delete(assessments);
        responseDto.setMessage("Assessment delete!");
        responseDto.setStatus("OK");
        responseDto.setCode(200);
        return responseDto;
    }

    @Override
    public ApplicationResponseDto<List<AssessmentResponseDto>> getAssessmentsBySellerId(Long sellerId) {
        ApplicationResponseDto<List<AssessmentResponseDto>> responseDto = new ApplicationResponseDto<>();
        List<Assessments> assessments = assessmentRepository.findBySellerId(sellerId);
        List<AssessmentResponseDto> assessmentResponseDtos = new ArrayList<>();
        for (Assessments assessment: assessments){
            AssessmentResponseDto assessmentResponseDto = new AssessmentResponseDto();
            assessmentResponseDto.setAssessment(assessment.getAssessment());
            assessmentResponseDto.setDescription(assessment.getDescription());
            assessmentResponseDto.setLogin(userRepository.findById(assessment.getUserId()).orElseThrow(()->new RuntimeException("exception")).getLogin());
            assessmentResponseDto.setUserId(assessment.getUserId());
            assessmentResponseDtos.add(assessmentResponseDto);
        }
        responseDto.setMessage("Assessment find!");
        responseDto.setStatus("OK");
        responseDto.setCode(200);
        responseDto.setPayload(assessmentResponseDtos);
        return responseDto;
    }
}
