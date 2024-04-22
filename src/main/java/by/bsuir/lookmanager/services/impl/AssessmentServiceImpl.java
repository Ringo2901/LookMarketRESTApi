package by.bsuir.lookmanager.services.impl;

import by.bsuir.lookmanager.dao.AssessmentRepository;
import by.bsuir.lookmanager.dao.UserRepository;
import by.bsuir.lookmanager.dto.ApplicationResponseDto;
import by.bsuir.lookmanager.dto.user.AssessmentRequestDto;
import by.bsuir.lookmanager.dto.user.AssessmentResponseDto;
import by.bsuir.lookmanager.dto.user.mapper.AssessmentMapper;
import by.bsuir.lookmanager.entities.user.UserEntity;
import by.bsuir.lookmanager.entities.user.information.Assessments;
import by.bsuir.lookmanager.exceptions.AlreadyExistsException;
import by.bsuir.lookmanager.exceptions.NotFoundException;
import by.bsuir.lookmanager.services.AssessmentService;
import com.moesif.api.models.UserBuilder;
import com.moesif.api.models.UserModel;
import com.moesif.servlet.MoesifFilter;
import jakarta.servlet.Filter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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
    private static final Logger LOGGER = LogManager.getLogger(AssessmentServiceImpl.class);
    @Autowired
    private Filter moesifFilter;

    @Override
    public ApplicationResponseDto<?> addAssessment(Long userId, AssessmentRequestDto requestDto) throws AlreadyExistsException {
        ApplicationResponseDto<?> responseDto = new ApplicationResponseDto<>();
        LOGGER.info("Check assessment exist by user id = " + userId + " and seller id = " + requestDto.getSellerId());
        if (assessmentRepository.existsByUserIdAndSellerId(userId, requestDto.getSellerId())) {
            LOGGER.warn("Assessment already exist by user id = " + userId + " and seller id = " + requestDto.getSellerId() + " when addAssessment execute");
            throw new AlreadyExistsException("Assessment already exists");
        }
        Assessments assessments = assessmentMapper.requestDtoToAssessmentEntity(requestDto);
        assessments.setUserId(userId);
        LOGGER.info("Save assessment with user id = " + userId + " and seller id = " + requestDto.getSellerId() + " to database");

        assessmentRepository.save(assessments);

        UserEntity user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("User not found!"));
        try {
            UserModel userModel = new UserBuilder()
                    .userId(String.valueOf(user.getId()))
                    .metadata(user)
                    .build();

            MoesifFilter filter = (MoesifFilter) moesifFilter;
            filter.updateUser(userModel);
        } catch (Throwable e) {
            LOGGER.warn("Failed to send user data");
        }
        responseDto.setMessage("Assessment add!");
        responseDto.setStatus("OK");
        responseDto.setCode(201);
        return responseDto;
    }

    @Override
    public ApplicationResponseDto<?> removeAssessment(Long userId, Long sellerId) throws NotFoundException {
        ApplicationResponseDto<?> responseDto = new ApplicationResponseDto<>();
        LOGGER.info("Check assessment exist by user id = " + userId + " and seller id = " + sellerId);
        Assessments assessments = assessmentRepository.findFirstByUserIdAndSellerId(userId, sellerId).orElseThrow(() ->
                new NotFoundException("Assessment with user id = " + userId + " and seller id = " + sellerId + " not found when removeAssessment execute!"));
        LOGGER.info("Assessment delete with user id = " + userId + " and seller id = " + sellerId);
        assessmentRepository.delete(assessments);
        UserEntity user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("User not found!"));
        try {
            UserModel userModel = new UserBuilder()
                    .userId(String.valueOf(user.getId()))
                    .metadata(user)
                    .build();

            MoesifFilter filter = (MoesifFilter) moesifFilter;
            filter.updateUser(userModel);
        } catch (Throwable e) {
            LOGGER.warn("Failed to send user data");
        }
        responseDto.setMessage("Assessment delete!");
        responseDto.setStatus("OK");
        responseDto.setCode(200);
        return responseDto;
    }

    @Override
    public ApplicationResponseDto<List<AssessmentResponseDto>> getAssessmentsBySellerId(Long sellerId) throws NotFoundException {
        ApplicationResponseDto<List<AssessmentResponseDto>> responseDto = new ApplicationResponseDto<>();
        LOGGER.info("Find assessment with seller id = " + sellerId);
        List<Assessments> assessments = assessmentRepository.findBySellerId(sellerId);
        List<AssessmentResponseDto> assessmentResponseDtos = new ArrayList<>();
        for (Assessments assessment : assessments) {
            AssessmentResponseDto assessmentResponseDto = new AssessmentResponseDto();
            assessmentResponseDto.setAssessment(assessment.getAssessment());
            assessmentResponseDto.setDescription(assessment.getDescription());
            LOGGER.info("Set user login for assessment with id = " + assessment.getId());
            assessmentResponseDto.setLogin(userRepository.findById(assessment.getUserId()).orElseThrow(() -> new NotFoundException("User with user id = " + assessment.getUserId() + " not found when getAssessmentsBySellerId execute!")).getLogin());
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
