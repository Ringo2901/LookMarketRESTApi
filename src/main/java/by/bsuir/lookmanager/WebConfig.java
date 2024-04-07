package by.bsuir.lookmanager;

import by.bsuir.lookmanager.dto.product.general.mapper.TupleToGeneralProductResponseDtoConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Autowired
    private TupleToGeneralProductResponseDtoConverter tupleToGeneralProductResponseDtoConverter;

    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(tupleToGeneralProductResponseDtoConverter);
    }
}
