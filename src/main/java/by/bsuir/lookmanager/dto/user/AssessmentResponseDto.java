package by.bsuir.lookmanager.dto.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AssessmentResponseDto {
    private String description;
    private Double assessment;
    private String login;
    private Long userId;
}
