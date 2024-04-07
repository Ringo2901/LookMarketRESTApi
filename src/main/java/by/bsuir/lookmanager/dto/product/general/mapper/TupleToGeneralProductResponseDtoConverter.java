package by.bsuir.lookmanager.dto.product.general.mapper;

import by.bsuir.lookmanager.dto.product.general.GeneralProductResponseDto;
import by.bsuir.lookmanager.enums.ProductStatus;
import org.postgresql.core.Tuple;
import org.springframework.stereotype.Component;
import org.springframework.core.convert.converter.Converter;

import java.sql.Timestamp;

@Component
public class TupleToGeneralProductResponseDtoConverter implements Converter<Tuple, GeneralProductResponseDto> {

    @Override
    public GeneralProductResponseDto convert(Tuple tuple) {
        GeneralProductResponseDto productDto = new GeneralProductResponseDto();
        productDto.setId(Long.getLong(new String(tuple.get(0))));
        productDto.setStatus(ProductStatus.valueOf(new String(tuple.get(1))));
        productDto.setTitle(new String(tuple.get(2)));
        productDto.setPrice(Double.parseDouble(new String(tuple.get(3))));
        productDto.setCreatedTime(Timestamp.valueOf(new String(tuple.get(4))));
        productDto.setUpdateTime(Timestamp.valueOf(new String(tuple.get(5))));
        productDto.setSubCategoryName(new String(tuple.get(6)));
        productDto.setCategoryName(new String(tuple.get(7)));
        productDto.setUserId(Long.getLong(new String(tuple.get(8))));
        productDto.setLogin(new String(tuple.get(9)));
        productDto.setImageId(Long.getLong(new String(tuple.get(10))));
        productDto.setImageData(new String(tuple.get(11)));
        return productDto;
    }
}