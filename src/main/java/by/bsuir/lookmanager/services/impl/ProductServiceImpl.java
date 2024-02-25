package by.bsuir.lookmanager.services.impl;

import by.bsuir.lookmanager.dao.ProductRepository;
import by.bsuir.lookmanager.dto.ApplicationResponseDto;
import by.bsuir.lookmanager.dto.product.ProductResponseDto;
import by.bsuir.lookmanager.dto.product.mapper.ProductInformationMapper;
import by.bsuir.lookmanager.entities.product.ProductEntity;
import by.bsuir.lookmanager.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProductServiceImpl implements ProductService {
    @Autowired
    ProductRepository productRepository;
    @Autowired
    ProductInformationMapper productInformationMapper;

    @Override
    public ApplicationResponseDto<ProductResponseDto> getProductInformationById(Long id) {
        ApplicationResponseDto<ProductResponseDto> responseDto = new ApplicationResponseDto<>();
        ProductEntity product = productRepository.findById(id).orElse(null);
        if (product == null) {
            responseDto.setCode(400);
            responseDto.setStatus("ERROR");
            responseDto.setMessage("Product not found!");
        } else {
            responseDto.setCode(200);
            responseDto.setStatus("OK");
            responseDto.setMessage("Product found!");
            responseDto.setPayload(productInformationMapper.productEntityToResponseDto(product));
        }
        return responseDto;
    }
}
