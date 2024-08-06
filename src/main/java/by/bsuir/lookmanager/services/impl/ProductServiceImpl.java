package by.bsuir.lookmanager.services.impl;

import by.bsuir.lookmanager.dao.*;
import by.bsuir.lookmanager.dao.specification.ProductSpecification;
import by.bsuir.lookmanager.dto.ApplicationResponseDto;
import by.bsuir.lookmanager.dto.ListResponseDto;
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
import by.bsuir.lookmanager.enums.AgeType;
import by.bsuir.lookmanager.enums.Condition;
import by.bsuir.lookmanager.enums.ProductGender;
import by.bsuir.lookmanager.enums.Season;
import by.bsuir.lookmanager.exceptions.BadParameterValueException;
import by.bsuir.lookmanager.exceptions.NotFoundException;
import by.bsuir.lookmanager.services.ProductService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.UriUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Service
@CacheConfig(cacheNames = "recommendedProducts")
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
    private static final Logger LOGGER = LogManager.getLogger(ProductServiceImpl.class);

    @Override
    public ApplicationResponseDto<ProductDetailsResponseDto> getProductInformationById(Long userId, Long id) throws NotFoundException {
        ApplicationResponseDto<ProductDetailsResponseDto> responseDto = new ApplicationResponseDto<>();
        LOGGER.info("Find product by id = " + id);
        ProductEntity product = productRepository.findById(id).orElse(null);
        if (product == null) {
            throw new NotFoundException("Product with id = " + id + " not found when getProductInformationById execute!");
        } else {
            if (!Objects.equals(userId, product.getCatalog().getUser().getId())){
                Integer viewNumber = product.getProductInformation().getViewNumber();
                if (viewNumber == null){
                    product.getProductInformation().setViewNumber(1);
                } else {
                    product.getProductInformation().setViewNumber(viewNumber + 1);
                }
                productInformationRepository.save(product.getProductInformation());
            }
            responseDto.setCode(200);
            responseDto.setStatus("OK");
            responseDto.setMessage("Product found!");
            ProductDetailsResponseDto productResponseDto = productDetailsResponseMapper.productEntityToResponseDto(product);
            List<ImageData> images = imageDataRepository.findByProductId(product.getId());
            LOGGER.info("Fill product images for product with id = " + id);
            List<String> urls = new ArrayList<>();
            for (ImageData image : images) {
                if (image.getImageUrl() != null) {
                    urls.add(image.getImageUrl());
                }
            }
            productResponseDto.setImagesUrls(urls);
            productResponseDto.setFavourite(favouritesRepository.existsByUserIdAndProductId(userId, id));
            responseDto.setPayload(productResponseDto);
        }
        return responseDto;
    }

    @Override
    public ApplicationResponseDto<ListResponseDto<GeneralProductResponseDto>> getProducts(Long userId, Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) throws NotFoundException, SQLException {
        ApplicationResponseDto<ListResponseDto<GeneralProductResponseDto>> responseDto = new ApplicationResponseDto<>();
        Specification<ProductEntity> spec = productSpecification.byUserId(userId);
        Pageable pageable;
        if (sortOrder != null && sortOrder.equals("asc")) {
            pageable = PageRequest.of(pageNumber, pageSize, Sort.by(sortBy).ascending());
        } else {
            pageable = PageRequest.of(pageNumber, pageSize, Sort.by(sortBy).descending());
        }
        LOGGER.info("Set pageable for getProducts = " + pageable);
        List<ProductEntity> responseEntityList = productRepository.findAll(spec, pageable).toList();
        Long totalItems = productNativeRepository.getProductCount(userId,"", sortBy, sortOrder, null,null,null,null, null, null, null, null, null, null, 0d, 1000000d);
        LOGGER.info("Find all products with pagination");
        return getListApplicationResponseDto(userId, responseDto, responseEntityList, totalItems);
    }

    @Override
    public ApplicationResponseDto<ListResponseDto<GeneralProductResponseDto>> getProductsByCategory(Long userId, String sex, Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) throws NotFoundException, SQLException {
        ApplicationResponseDto<ListResponseDto<GeneralProductResponseDto>> responseDto = new ApplicationResponseDto<>();
        Specification<ProductEntity> spec = productSpecification.byCategoryId(sex, userId.toString());
        Pageable pageable;
        if (sortOrder != null && sortOrder.equals("asc")) {
            pageable = PageRequest.of(pageNumber, pageSize, Sort.by(sortBy).ascending());
        } else {
            pageable = PageRequest.of(pageNumber, pageSize, Sort.by(sortBy).descending());
        }
        LOGGER.info("Set pageable for getProducts = " + pageable);
        List<ProductEntity> responseEntityList = productRepository.findAll(spec, pageable).toList();
        String[] sexes = {sex};
        Long totalItems = productNativeRepository.getProductCount(userId,"", sortBy, sortOrder, null,null,null,null, sexes, null, null, null, null, null, 0d, 1000000d);
        LOGGER.info("Find all products with sex = " + sex);
        return getListApplicationResponseDto(userId, responseDto, responseEntityList, totalItems);
    }

    @Override
    public ApplicationResponseDto<ListResponseDto<GeneralProductResponseDto>> getProductsWithSorting(Long userId, String query, Integer pageSize, Integer pageNumber, String sortBy, String sortOrder,
                                                                                          List<String> size, List<String> color, List<String> brand, List<String> filtSeason, List<String> filtGender,
                                                                                          List<String> filtAgeType, List<String> tags, List<String> materials, List<String> subcategory, List<String> category,
                                                                                          Double minPrice, Double maxPrice) throws SQLException {
        LOGGER.info("Get products with sorting, filtering and pagination");
        String decodedQuery = "";
        if (query != null) {
            decodedQuery = UriUtils.decode(query, "UTF-8");
        }
        List<Integer> sizes = new ArrayList<>();
        if (size!=null) {
            for (String name : size) {
                sizes.add(Integer.parseInt(name));
            }
        } else {
            sizes=null;
        }
        List<GeneralProductResponseDto> products = productNativeRepository.getProducts(userId, decodedQuery, pageSize, pageNumber, sortBy, sortOrder, sizes != null ? sizes.toArray(new Integer[sizes.size()]) : null, color != null ? color.toArray(new String[color.size()]) : null, brand != null ? brand.toArray(new String[brand.size()]) : null, filtSeason != null ? filtSeason.toArray(new String[filtSeason.size()]) : null, filtGender != null ? filtGender.toArray(new String[0]) : null, filtAgeType != null ? filtAgeType.toArray(new String[0]) : null, tags != null ? tags.toArray(new String[0]) : null, materials != null ? materials.toArray(new String[0]) : null, subcategory != null ? subcategory.toArray(new String[0]) : null, category != null ? category.toArray(new String[0]) : null, minPrice, maxPrice);

        LOGGER.info("Set favourites");
        for (GeneralProductResponseDto product : products) {
            product.setFavourite(favouritesRepository.existsByUserIdAndProductId(userId, product.getId()));
        }
        ApplicationResponseDto<ListResponseDto<GeneralProductResponseDto>> responseDto = new ApplicationResponseDto<>();
        ListResponseDto<GeneralProductResponseDto> listResponseDto = new ListResponseDto<>();
        Long totalItems = productNativeRepository.getProductCount(userId, decodedQuery, sortBy, sortOrder, sizes != null ? sizes.toArray(new Integer[sizes.size()]) : null, color != null ? color.toArray(new String[color.size()]) : null, brand != null ? brand.toArray(new String[brand.size()]) : null, filtSeason != null ? filtSeason.toArray(new String[filtSeason.size()]) : null, filtGender != null ? filtGender.toArray(new String[0]) : null, filtAgeType != null ? filtAgeType.toArray(new String[0]) : null, tags != null ? tags.toArray(new String[0]) : null, materials != null ? materials.toArray(new String[0]) : null, subcategory != null ? subcategory.toArray(new String[0]) : null, category != null ? category.toArray(new String[0]) : null, minPrice, maxPrice);
        listResponseDto.setItems(products);
        listResponseDto.setTotalItems(totalItems);
        responseDto.setStatus("Product found!");
        responseDto.setCode(200);
        responseDto.setStatus("OK");
        responseDto.setPayload(listResponseDto);
        return responseDto;
    }

    @Override
    @Transactional
    @CacheEvict(value = "recommendedProducts", allEntries = true)
    public ApplicationResponseDto<Long> saveProduct(ProductDetailsRequestDto requestDto) {
        ApplicationResponseDto<Long> responseDto = new ApplicationResponseDto<>();
        ProductEntity entityToSave = productDetailsRequestMapper.productRequestDtoToEntity(requestDto);
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm");
        try {
            Date parsedDate = dateFormat.parse(requestDto.getCreatedTime());
            Timestamp timestamp = new Timestamp(parsedDate.getTime());
            entityToSave.setCreatedTime(timestamp);
        } catch (ParseException e) {
            throw new BadParameterValueException("Bad time on request!");
        }
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
        LOGGER.info("Save product with new id = " + product.getId());
        responseDto.setCode(201);
        responseDto.setStatus("OK");
        responseDto.setMessage("Product save!");
        //ProductDetailsResponseDto productResponseDto = productDetailsResponseMapper.productEntityToResponseDto(product);
        //productResponseDto.setFavourite(false);
        responseDto.setPayload(product.getId());
        return responseDto;
    }

    @Override
    @CacheEvict(value = "recommendedProducts", allEntries = true)
    public ApplicationResponseDto<ProductDetailsResponseDto> updateProduct(Long userId, Long id, ProductInformationRequestDto requestDto) throws NotFoundException {
        ApplicationResponseDto<ProductDetailsResponseDto> responseDto = new ApplicationResponseDto<>();
        ProductEntity entityToUpdate = productRepository.findById(id).orElseThrow(() -> new NotFoundException("Product with id = " + id + " not found when updateProduct execute!"));
        ProductInformation productInformation = entityToUpdate.getProductInformation();
        productInformation.setDescription(requestDto.getDescription());
        productInformation.setPrice(requestDto.getPrice());
        if (requestDto.getGender().isEmpty()){
            productInformation.setGender(null);
        } else {
            productInformation.setGender(ProductGender.valueOf(requestDto.getGender()));
        }
        if (requestDto.getSeason().isEmpty()){
            productInformation.setSeason(null);
        } else {
            productInformation.setSeason(requestDto.getSeason().equals("DEMI-SEASON") ? Season.DEMI_SEASON : Season.valueOf(requestDto.getSeason()));
        }
        if (requestDto.getCondition().isEmpty()){
            productInformation.setCondition(null);
        } else {
            productInformation.setCondition(Condition.valueOf(requestDto.getCondition()));
        }
        if (requestDto.getAgeType().isEmpty()){
            productInformation.setAgeType(null);
        } else {
            productInformation.setAgeType(AgeType.valueOf(requestDto.getAgeType()));
        }
        productInformation.setProductBrand(brandRepository.getReferenceById(requestDto.getBrandId()));
        productInformation.setColors(colorRepository.findAllById(requestDto.getColorsId()));
        productInformation.setTags(tagRepository.findAllById(requestDto.getTagsId()));
        productInformation.setMaterials(materialRepository.findAllById(requestDto.getMaterialsId()));
        productInformation.setSizes(sizeRepository.findAllById(requestDto.getSizesId()));
        entityToUpdate.setProductInformation(productInformation);
        entityToUpdate.setUpdateTime(new Timestamp(System.currentTimeMillis()));
        ProductEntity product = productRepository.save(entityToUpdate);
        LOGGER.info("Product with id = " + id + " updated");
        responseDto.setCode(201);
        responseDto.setStatus("OK");
        responseDto.setMessage("Product update!");
        ProductDetailsResponseDto productResponseDto = productDetailsResponseMapper.productEntityToResponseDto(product);
        productResponseDto.setFavourite(favouritesRepository.existsByUserIdAndProductId(userId, id));
        responseDto.setPayload(productResponseDto);
        return responseDto;
    }

    @Override
    @CacheEvict(value = "recommendedProducts", allEntries = true)
    @Transactional
    public ApplicationResponseDto<Object> deleteProduct(Long id) {
        ApplicationResponseDto<Object> responseDto = new ApplicationResponseDto<>();
        LOGGER.info("Delete product with id = " + id);
        productInformationRepository.deleteById(productRepository.findById(id).orElseThrow(() -> new NotFoundException("Product with id = " + id + " not found!")).getProductInformation().getId());
        productRepository.deleteById(id);
        responseDto.setCode(200);
        responseDto.setStatus("OK");
        responseDto.setMessage("Product delete!");
        return responseDto;
    }

    private ApplicationResponseDto<ListResponseDto<GeneralProductResponseDto>> getListApplicationResponseDto(Long userId, ApplicationResponseDto<ListResponseDto<GeneralProductResponseDto>> responseDto, List<ProductEntity> responseEntityList, Long totalItems) throws NotFoundException {
        responseDto.setCode(200);
        responseDto.setMessage("Products found!");
        responseDto.setStatus("OK");
        if (!responseEntityList.isEmpty()) {
            List<GeneralProductResponseDto> generalProductResponseDtos = productListMapper.toGeneralProductResponseDtoList(responseEntityList);
            for (GeneralProductResponseDto generalProductResponseDto : generalProductResponseDtos) {
                LOGGER.info("Find media for product with id = " + generalProductResponseDto.getId());
                ImageDataResponseDto imageDataResponseDto = imageDataToDtoMapper.mediaToDto(imageDataRepository.findFirstByProductId(generalProductResponseDto.getId()));
                generalProductResponseDto.setImageUrl(imageDataResponseDto == null ? null : imageDataResponseDto.getImageUrl());
                generalProductResponseDto.setImageId(imageDataResponseDto == null ? null : imageDataResponseDto.getId());
                LOGGER.info("Set favourite flag for product with id = " + generalProductResponseDto.getId());
                generalProductResponseDto.setFavourite(favouritesRepository.existsByUserIdAndProductId(userId, generalProductResponseDto.getId()));
                Double price = generalProductResponseDto.getPrice();
                if (price != null) {
                    BigDecimal roundedPrice = BigDecimal.valueOf(price).setScale(2, RoundingMode.HALF_UP);
                    generalProductResponseDto.setPrice(roundedPrice.doubleValue());
                }
            }
            ListResponseDto<GeneralProductResponseDto> listResponseDto = new ListResponseDto<>();
            listResponseDto.setItems(generalProductResponseDtos);
            listResponseDto.setTotalItems(totalItems);
            responseDto.setPayload(listResponseDto);
        } else {
            responseDto.setPayload(null);
            //throw new NotFoundException("Products not found!");
        }
        return responseDto;
    }
}
