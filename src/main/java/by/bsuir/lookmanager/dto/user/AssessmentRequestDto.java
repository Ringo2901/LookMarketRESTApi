package by.bsuir.lookmanager.dto.user;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AssessmentRequestDto {
    private String description;
    @NotBlank
    @DecimalMax("5")
    @DecimalMin("0")
    private Integer assessment;
    @NotBlank
    private Long sellerId;
}
