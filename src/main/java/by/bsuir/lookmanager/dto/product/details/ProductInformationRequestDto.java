package by.bsuir.lookmanager.dto.product.details;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductInformationRequestDto {
    @NotBlank
    private Double price;

    private String description;
    private Integer genderId;
    private Integer seasonId;
    private Integer conditionId;
    private Integer ageTypeId;
    private Long brandId;

    private List<Long> sizesId;
    private List<Long> colorsId;
    private List<Long> materialsId;
    private List<Long> tagsId;
}
