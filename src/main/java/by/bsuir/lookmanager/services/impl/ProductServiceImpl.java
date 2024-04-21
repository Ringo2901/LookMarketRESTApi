package by.bsuir.lookmanager.services.impl;

import by.bsuir.lookmanager.dao.*;
import by.bsuir.lookmanager.dao.specification.ProductSpecification;
import by.bsuir.lookmanager.dto.ApplicationResponseDto;
import by.bsuir.lookmanager.dto.product.details.ProductDetailsRequestDto;
import by.bsuir.lookmanager.dto.product.details.ProductDetailsResponseDto;
import by.bsuir.lookmanager.dto.product.details.ProductInformationRequestDto;
import by.bsuir.lookmanager.dto.product.details.mapper.ProductDetailsRequestMapper;
import by.bsuir.lookmanager.dto.product.details.mapper.ProductDetailsResponseMapper;
import by.bsuir.lookmanager.dto.product.general.GeneralProductResponseDto;
import by.bsuir.lookmanager.dto.product.general.mapper.ProductListMapper;
import by.bsuir.lookmanager.dto.product.media.ImageDataResponseDto;
import by.bsuir.lookmanager.dto.product.media.mapper.ImageDataToDtoMapper;
import by.bsuir.lookmanager.entities.product.ProductEntity;
import by.bsuir.lookmanager.entities.product.information.*;
import by.bsuir.lookmanager.entities.user.information.Catalog;
import by.bsuir.lookmanager.exceptions.NotFoundException;
import by.bsuir.lookmanager.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private ProductNativeRepository productNativeRepository;
    @Autowired
    private ProductDetailsResponseMapper productDetailsResponseMapper;
    @Autowired
    private ProductListMapper productListMapper;
    @Autowired
    private ProductSpecification productSpecification;
    @Autowired
    private ProductDetailsRequestMapper productDetailsRequestMapper;
    @Autowired
    private ProductInformationRepository productInformationRepository;
    @Autowired
    private ColorRepository colorRepository;
    @Autowired
    private TagRepository tagRepository;
    @Autowired
    private SizeRepository sizeRepository;
    @Autowired
    private MaterialRepository materialRepository;
    @Autowired
    private BrandRepository brandRepository;
    @Autowired
    private SubCategoryRepository subCategoryRepository;
    @Autowired
    private CatalogRepository catalogRepository;
    @Autowired
    private ImageDataRepository imageDataRepository;
    @Autowired
    private ImageDataToDtoMapper imageDataToDtoMapper;
    @Autowired
    private FavouritesRepository favouritesRepository;

    @Override
    public ApplicationResponseDto<ProductDetailsResponseDto> getProductInformationById(Long userId, Long id) throws NotFoundException {
        ApplicationResponseDto<ProductDetailsResponseDto> responseDto = new ApplicationResponseDto<>();
        ProductEntity product = productRepository.findById(id).orElse(null);
        if (product == null) {
            throw new NotFoundException("Product not found!");
        } else {
            responseDto.setCode(200);
            responseDto.setStatus("OK");
            responseDto.setMessage("Product found!");
            ProductDetailsResponseDto productResponseDto = productDetailsResponseMapper.productEntityToResponseDto(product);
            productResponseDto.setFavourite(favouritesRepository.existsByUserIdAndProductId(userId, id));
            responseDto.setPayload(productResponseDto);
        }
        return responseDto;
    }

    @Override
    public ApplicationResponseDto<List<GeneralProductResponseDto>> getProducts(Long userId, Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) throws NotFoundException {
        ApplicationResponseDto<List<GeneralProductResponseDto>> responseDto = new ApplicationResponseDto<>();
        Pageable pageable;
        if (sortOrder != null && sortOrder.equals("asc")) {
            pageable = PageRequest.of(pageNumber, pageSize, Sort.by(sortBy).ascending());
        } else {
            pageable = PageRequest.of(pageNumber, pageSize, Sort.by(sortBy).descending());
        }
        List<ProductEntity> responseEntityList = productRepository.findAll(pageable).toList();
        return getListApplicationResponseDto(userId, responseDto, responseEntityList);
    }

    @Override
    public ApplicationResponseDto<List<GeneralProductResponseDto>> getProductsByCategory(Long userId, String sex, Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) throws NotFoundException {
        ApplicationResponseDto<List<GeneralProductResponseDto>> responseDto = new ApplicationResponseDto<>();
        Specification<ProductEntity> spec = productSpecification.byCategoryId(sex);
        Pageable pageable;
        if (sortOrder != null && sortOrder.equals("asc")) {
            pageable = PageRequest.of(pageNumber, pageSize, Sort.by(sortBy).ascending());
        } else {
            pageable = PageRequest.of(pageNumber, pageSize, Sort.by(sortBy).descending());
        }
        List<ProductEntity> responseEntityList = productRepository.findAll(spec, pageable).toList();
        return getListApplicationResponseDto(userId, responseDto, responseEntityList);
    }

    @Override
    public ApplicationResponseDto<List<GeneralProductResponseDto>> getProductsWithSorting(Long userId, String query, Integer pageSize, Integer pageNumber, String sortBy, String sortOrder,
                                                                                          List<Integer> size, List<String> color, List<String> brand, List<String> filtSeason, List<String> filtGender,
                                                                                          List<String> filtAgeType, List<String> tags, List<String> materials, List<String> subcategory, List<String> category,
                                                                                          Double minPrice, Double maxPrice) throws SQLException {
        List<GeneralProductResponseDto> products = productNativeRepository.getProducts(query, pageSize, pageNumber, sortBy, sortOrder, size!=null?size.toArray(new Integer[size.size()]):null, color!=null?color.toArray(new String[color.size()]):null, brand!=null?brand.toArray(new String[brand.size()]):null, filtSeason!=null?filtSeason.toArray(new String[filtSeason.size()]):null, filtGender!=null?filtGender.toArray(new String[0]):null, filtAgeType!=null?filtAgeType.toArray(new String[0]):null, tags!=null?tags.toArray(new String[0]):null, materials!=null?materials.toArray(new String[0]):null, subcategory!=null?subcategory.toArray(new String[0]):null, category!=null?category.toArray(new String[0]):null, minPrice, maxPrice);
        for (GeneralProductResponseDto product: products){
            product.setFavourite(favouritesRepository.existsByUserIdAndProductId(userId, product.getId()));
        }
        ApplicationResponseDto<List<GeneralProductResponseDto>> responseDto = new ApplicationResponseDto<>();
        responseDto.setStatus("Product found!");
        responseDto.setCode(200);
        responseDto.setStatus("OK");
        responseDto.setPayload(products);
        return responseDto;
    }

    @Override
    @Transactional
    public ApplicationResponseDto<Long> saveProduct(ProductDetailsRequestDto requestDto) {
        ApplicationResponseDto<Long> responseDto = new ApplicationResponseDto<>();
        ProductEntity entityToSave = productDetailsRequestMapper.productRequestDtoToEntity(requestDto);
        Catalog catalog = catalogRepository.getReferenceById(requestDto.getCatalogId());
        entityToSave.setCatalog(catalog);
        ProductBrand brand = brandRepository.getReferenceById(requestDto.getBrandId());
        entityToSave.getProductInformation().setProductBrand(brand);
        SubCategory subCategory = subCategoryRepository.getReferenceById(requestDto.getSubCategoryId());
        entityToSave.setSubCategory(subCategory);
        List<ProductSize> sizes = sizeRepository.findAllById(requestDto.getSizesId());
        entityToSave.getProductInformation().setSizes(sizes);
        List<ProductTag> tags = tagRepository.findAllById(requestDto.getTagsId());
        entityToSave.getProductInformation().setTags(tags);
        List<ProductColor> colors = colorRepository.findAllById(requestDto.getColorsId());
        entityToSave.getProductInformation().setColors(colors);
        List<ProductMaterial> materials = materialRepository.findAllById(requestDto.getMaterialsId());
        entityToSave.getProductInformation().setMaterials(materials);
        productInformationRepository.save(entityToSave.getProductInformation());
        ProductEntity product = productRepository.save(entityToSave);
        responseDto.setCode(201);
        responseDto.setStatus("OK");
        responseDto.setMessage("Product save!");
        //ProductDetailsResponseDto productResponseDto = productDetailsResponseMapper.productEntityToResponseDto(product);
        //productResponseDto.setFavourite(false);
        responseDto.setPayload(product.getId());
        return responseDto;
    }

    @Override
    public ApplicationResponseDto<ProductDetailsResponseDto> updateProduct(Long userId, Long id, ProductInformationRequestDto requestDto) throws NotFoundException {
        ApplicationResponseDto<ProductDetailsResponseDto> responseDto = new ApplicationResponseDto<>();
        ProductEntity entityToUpdate = productRepository.findById(id).orElseThrow(() -> new NotFoundException("Product not found!"));
        ProductInformation productInformation = entityToUpdate.getProductInformation();
        productInformation.setDescription(requestDto.getDescription());
        productInformation.setPrice(requestDto.getPrice());
        productInformation.setGender(requestDto.getGender());
        productInformation.setSeason(requestDto.getSeason());
        productInformation.setCondition(requestDto.getCondition());
        productInformation.setAgeType(requestDto.getAgeType());
        productInformation.setProductBrand(brandRepository.getReferenceById(requestDto.getBrandId()));
        productInformation.setColors(colorRepository.findAllById(requestDto.getColorsId()));
        productInformation.setTags(tagRepository.findAllById(requestDto.getTagsId()));
        productInformation.setMaterials(materialRepository.findAllById(requestDto.getMaterialsId()));
        productInformation.setSizes(sizeRepository.findAllById(requestDto.getSizesId()));
        entityToUpdate.setProductInformation(productInformation);
        entityToUpdate.setUpdateTime(new Timestamp(System.currentTimeMillis()));
        ProductEntity product = productRepository.save(entityToUpdate);
        responseDto.setCode(201);
        responseDto.setStatus("OK");
        responseDto.setMessage("Product update!");
        ProductDetailsResponseDto productResponseDto = productDetailsResponseMapper.productEntityToResponseDto(product);
        productResponseDto.setFavourite(favouritesRepository.existsByUserIdAndProductId(userId, id));
        responseDto.setPayload(productResponseDto);
        return responseDto;
    }

    @Override
    public ApplicationResponseDto<Object> deleteProduct(Long id) {
        ApplicationResponseDto<Object> responseDto = new ApplicationResponseDto<>();
        productRepository.deleteById(id);
        responseDto.setCode(200);
        responseDto.setStatus("OK");
        responseDto.setMessage("Product delete!");
        return responseDto;
    }

    private ApplicationResponseDto<List<GeneralProductResponseDto>> getListApplicationResponseDto(Long userId, ApplicationResponseDto<List<GeneralProductResponseDto>> responseDto, List<ProductEntity> responseEntityList) throws NotFoundException {
        if (!responseEntityList.isEmpty()) {
            responseDto.setCode(200);
            responseDto.setMessage("Products found!");
            responseDto.setStatus("OK");
            List<GeneralProductResponseDto> generalProductResponseDtos = productListMapper.toGeneralProductResponseDtoList(responseEntityList);
            for (GeneralProductResponseDto generalProductResponseDto : generalProductResponseDtos) {
                ImageDataResponseDto imageDataResponseDto = imageDataToDtoMapper.mediaToDto(imageDataRepository.findFirstByProductId(generalProductResponseDto.getId()));
                generalProductResponseDto.setImageData(imageDataResponseDto == null ? null : imageDataResponseDto.getImageData());
                generalProductResponseDto.setImageId(imageDataResponseDto == null ? null : imageDataResponseDto.getId());
                generalProductResponseDto.setFavourite(favouritesRepository.existsByUserIdAndProductId(userId, generalProductResponseDto.getId()));
            }
            responseDto.setPayload(generalProductResponseDtos);
        } else {
            throw new NotFoundException("Products not found!");
        }
        return responseDto;
    }
}
