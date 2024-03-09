package by.bsuir.lookmanager.dto.product.media;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ImageDataRequestDto {
    @NotBlank
    private String imageData;
}
