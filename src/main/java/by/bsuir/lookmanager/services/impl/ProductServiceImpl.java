package by.bsuir.lookmanager.services.impl;

import by.bsuir.lookmanager.dao.ProductRepository;
import by.bsuir.lookmanager.dto.ApplicationResponseDto;
import by.bsuir.lookmanager.dto.product.general.GeneralProductResponseDto;
import by.bsuir.lookmanager.dto.product.details.ProductDetailsResponseDto;
import by.bsuir.lookmanager.dto.product.details.mapper.ProductDetailsMapper;
import by.bsuir.lookmanager.dto.product.general.mapper.ProductListMapper;
import by.bsuir.lookmanager.entities.product.ProductEntity;
import by.bsuir.lookmanager.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {
    @Autowired
    ProductRepository productRepository;
    @Autowired
    ProductDetailsMapper productDetailsMapper;
    @Autowired
    ProductListMapper productListMapper;

    @Override
    public ApplicationResponseDto<ProductDetailsResponseDto> getProductInformationById(Long id) {
        ApplicationResponseDto<ProductDetailsResponseDto> responseDto = new ApplicationResponseDto<>();
        ProductEntity product = productRepository.findById(id).orElse(null);
        if (product == null) {
            responseDto.setCode(400);
            responseDto.setStatus("ERROR");
            responseDto.setMessage("Product not found!");
        } else {
            responseDto.setCode(200);
            responseDto.setStatus("OK");
            responseDto.setMessage("Product found!");
            responseDto.setPayload(productDetailsMapper.productEntityToResponseDto(product));
        }
        return responseDto;
    }

    @Override
    public ApplicationResponseDto<List<GeneralProductResponseDto>> getProducts() {
        ApplicationResponseDto<List<GeneralProductResponseDto>> responseDto = new ApplicationResponseDto<>();
        responseDto.setPayload(productListMapper.toGeneralProductResponseDtoList((List<ProductEntity>) productRepository.findAll()));
        return responseDto;
    }
}
