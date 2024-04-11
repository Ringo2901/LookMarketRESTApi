package by.bsuir.lookmanager.dto.catalog;

import by.bsuir.lookmanager.dto.product.general.GeneralProductResponseDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CatalogWithItemsDto {
    private Long id;
    private String name;
    private List<GeneralProductResponseDto> responseDtoList;
}
