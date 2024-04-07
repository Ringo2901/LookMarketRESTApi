package by.bsuir.lookmanager.dto.product.general;

import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.Base64;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GeneralProductResponseDto {
    private Long id;
    private String title;
    @Temporal(TemporalType.TIMESTAMP)
    private Timestamp createdTime;

    @Temporal(TemporalType.TIMESTAMP)
    private Timestamp updateTime;
    private Double price;
    private String subCategoryName;
    private String categoryName;

    private String login;
    private Long userId;

    private Long imageId;
    private String imageData;
    //private boolean isFavourite;


    public void setImageData(byte[] imageData) {
        this.imageData = Base64.getEncoder().encodeToString(imageData);;
    }

    public void setImageData(String imageData) {
        this.imageData = imageData;;
    }
}
