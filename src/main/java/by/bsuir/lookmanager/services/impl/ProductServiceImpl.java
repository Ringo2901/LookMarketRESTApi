package by.bsuir.lookmanager.services.impl;

import by.bsuir.lookmanager.dao.MediaRepository;
import by.bsuir.lookmanager.dao.ProductRepository;
import by.bsuir.lookmanager.dao.specification.ProductSpecification;
import by.bsuir.lookmanager.dto.ApplicationResponseDto;
import by.bsuir.lookmanager.dto.product.details.ProductDetailsResponseDto;
import by.bsuir.lookmanager.dto.product.details.mapper.ProductDetailsMapper;
import by.bsuir.lookmanager.dto.product.general.GeneralProductResponseDto;
import by.bsuir.lookmanager.dto.product.general.mapper.ProductListMapper;
import by.bsuir.lookmanager.entities.product.ProductEntity;
import by.bsuir.lookmanager.entities.product.information.Media;
import by.bsuir.lookmanager.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private ProductDetailsMapper productDetailsMapper;
    @Autowired
    private ProductListMapper productListMapper;
    @Autowired
    private ProductSpecification productSpecification;
    @Autowired
    private MediaRepository mediaRepository;

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
            ProductDetailsResponseDto productResponseDto = productDetailsMapper.productEntityToResponseDto(product);
            List<Media> mediaList = mediaRepository.findByProductId(id);

            List<Long> mediaIds = mediaList.stream()
                    .map(Media::getId)
                    .collect(Collectors.toList());

            productResponseDto.setMediaId(mediaIds);
            responseDto.setPayload(productResponseDto);
        }
        return responseDto;
    }

    @Override
    public ApplicationResponseDto<List<GeneralProductResponseDto>> getProducts(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {
        ApplicationResponseDto<List<GeneralProductResponseDto>> responseDto = new ApplicationResponseDto<>();
        Pageable pageable;
        if (sortOrder != null && sortOrder.equals("asc")) {
            pageable = PageRequest.of(pageNumber, pageSize, Sort.by(sortBy).ascending());
        } else {
            pageable = PageRequest.of(pageNumber, pageSize, Sort.by(sortBy).descending());
        }
        List<ProductEntity> responseEntityList = productRepository.findAll(pageable).toList();
        return getListApplicationResponseDto(responseDto, responseEntityList);
    }

    @Override
    public ApplicationResponseDto<List<GeneralProductResponseDto>> getProductsByCategory(Long categoryId, Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {
        ApplicationResponseDto<List<GeneralProductResponseDto>> responseDto = new ApplicationResponseDto<>();
        Specification<ProductEntity> spec = productSpecification.byCategoryId(categoryId);
        Pageable pageable;
        if (sortOrder != null && sortOrder.equals("asc")) {
            pageable = PageRequest.of(pageNumber, pageSize, Sort.by(sortBy).ascending());
        } else {
            pageable = PageRequest.of(pageNumber, pageSize, Sort.by(sortBy).descending());
        }
        List<ProductEntity> responseEntityList = productRepository.findAll(spec, pageable).toList();
        return getListApplicationResponseDto(responseDto, responseEntityList);
    }

    private ApplicationResponseDto<List<GeneralProductResponseDto>> getListApplicationResponseDto(ApplicationResponseDto<List<GeneralProductResponseDto>> responseDto, List<ProductEntity> responseEntityList) {
        if (!responseEntityList.isEmpty()) {
            responseDto.setCode(200);
            responseDto.setMessage("Products found!");
            responseDto.setStatus("OK");
            List<GeneralProductResponseDto> generalProductResponseDtos = productListMapper.toGeneralProductResponseDtoList(responseEntityList);
            for (GeneralProductResponseDto productDto : generalProductResponseDtos) {
                Long productId = productDto.getId();
                Media media = mediaRepository.findFirstByProductId(productId);
                if (media!=null) {
                    productDto.setProductMediaId(media.getId());
                }
            }
            responseDto.setPayload(generalProductResponseDtos);
        } else {
            responseDto.setCode(400);
            responseDto.setMessage("Products not found!");
            responseDto.setStatus("ERROR");
        }
        return responseDto;
    }
}
