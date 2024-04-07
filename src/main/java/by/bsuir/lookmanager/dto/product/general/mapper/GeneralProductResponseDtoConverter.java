package by.bsuir.lookmanager.dto.product.general.mapper;

import by.bsuir.lookmanager.dto.product.general.GeneralProductResponseDto;
import by.bsuir.lookmanager.enums.ProductStatus;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.List;

@Component
public class GeneralProductResponseDtoConverter implements Converter<List<Object>, GeneralProductResponseDto> {

    @Override
    public GeneralProductResponseDto convert(List<Object> result) {
        GeneralProductResponseDto dto = new GeneralProductResponseDto();
        dto.setId((Long) result.get(0));
        dto.setStatus(ProductStatus.valueOf((String) result.get(1)));
        dto.setTitle((String) result.get(2));
        dto.setPrice((Double) result.get(3));
        dto.setCreatedTime((Timestamp) result.get(4));
        dto.setUpdateTime((Timestamp) result.get(5));
        dto.setSubCategoryName((String) result.get(6));
        dto.setCategoryName((String) result.get(7));
        dto.setUserId((Long) result.get(8));
        dto.setLogin((String) result.get(9));
        dto.setImageId((Long) result.get(10));
        dto.setImageData((String) result.get(11));
        return dto;
    }
}