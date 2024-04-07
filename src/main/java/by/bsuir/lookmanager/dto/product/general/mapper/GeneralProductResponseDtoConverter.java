package by.bsuir.lookmanager.dto.product.general.mapper;

import by.bsuir.lookmanager.dto.product.general.GeneralProductResponseDto;
import by.bsuir.lookmanager.enums.ProductStatus;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

@Component
public class GeneralProductResponseDtoConverter implements Converter<Map<String, Object>, GeneralProductResponseDto> {

    @Override
    public GeneralProductResponseDto convert(Map<String, Object> result) {
        GeneralProductResponseDto dto = new GeneralProductResponseDto();
        dto.setId((Long) result.get("id"));
        dto.setStatus(ProductStatus.valueOf((String) result.get("status")));
        dto.setTitle((String) result.get("title"));
        dto.setPrice((Double) result.get("price"));
        dto.setCreatedTime((Timestamp) result.get("createdTime"));
        dto.setUpdateTime((Timestamp) result.get("updateTime"));
        dto.setSubCategoryName((String) result.get("subcategoryname"));
        dto.setCategoryName((String) result.get("categoryname"));
        dto.setUserId((Long) result.get("userid"));
        dto.setLogin((String) result.get("login"));
        dto.setImageId((Long) result.get("imageid"));
        dto.setImageData((String) result.get("imagedata"));
        return dto;
    }
}