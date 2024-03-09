package by.bsuir.lookmanager.dto.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AssessmentRequestDto {
    private String description;
    private Integer assessment;
    private Long sellerId;
}
