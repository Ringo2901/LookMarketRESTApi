package by.bsuir.lookmanager.dto.configuration;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CountryWithCityDto {
    private Integer countryId;
    private String countryName;
    private List<CityDto> cityDtoList;
}
