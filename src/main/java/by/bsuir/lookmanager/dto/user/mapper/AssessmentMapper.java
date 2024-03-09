package by.bsuir.lookmanager.dto.user.mapper;

import by.bsuir.lookmanager.dto.user.AssessmentRequestDto;
import by.bsuir.lookmanager.entities.user.information.Assessments;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AssessmentMapper {
    Assessments requestDtoToAssessmentEntity (AssessmentRequestDto requestDto);
}
