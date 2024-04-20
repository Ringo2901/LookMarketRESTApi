package by.bsuir.lookmanager.services.impl;

import by.bsuir.lookmanager.dao.ImageDataRepository;
import by.bsuir.lookmanager.dao.ProductRepository;
import by.bsuir.lookmanager.dao.UserRepository;
import by.bsuir.lookmanager.dto.ApplicationResponseDto;
import by.bsuir.lookmanager.dto.product.general.GeneralProductResponseDto;
import by.bsuir.lookmanager.dto.product.general.mapper.ProductListMapper;
import by.bsuir.lookmanager.dto.product.media.ImageDataResponseDto;
import by.bsuir.lookmanager.dto.product.media.mapper.ImageDataToDtoMapper;
import by.bsuir.lookmanager.entities.product.ProductEntity;
import by.bsuir.lookmanager.entities.user.UserEntity;
import by.bsuir.lookmanager.exceptions.AlreadyExistsException;
import by.bsuir.lookmanager.exceptions.NotFoundException;
import by.bsuir.lookmanager.services.FavoritesService;
import org.aspectj.weaver.ast.Not;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class FavoritesServiceImpl implements FavoritesService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private ProductListMapper productListMapper;
    @Autowired
    private ImageDataRepository imageDataRepository;
    @Autowired
    private ImageDataToDtoMapper imageDataToDtoMapper;

    @Override
    public ApplicationResponseDto<List<GeneralProductResponseDto>> getFavoritesByUserId(Long id) throws NotFoundException {
        ApplicationResponseDto<List<GeneralProductResponseDto>> responseDto = new ApplicationResponseDto<>();
        UserEntity user = userRepository.findById(id).orElseThrow(() -> new NotFoundException("User not found!"));

        responseDto.setMessage("User found!");
        responseDto.setStatus("OK");
        responseDto.setCode(200);
        List<GeneralProductResponseDto> generalProductResponseDtos = productListMapper.toGeneralProductResponseDtoList(user.getFavouriteProducts());
        for (GeneralProductResponseDto generalProductResponseDto : generalProductResponseDtos) {
            ImageDataResponseDto imageDataResponseDto = imageDataToDtoMapper.mediaToDto(imageDataRepository.findFirstByProductId(generalProductResponseDto.getId()));
            //generalProductResponseDto.setImageData(imageDataResponseDto == null ? null : imageDataResponseDto.getImageData());
            generalProductResponseDto.setImageId(imageDataResponseDto == null ? null : imageDataResponseDto.getId());
        }
        responseDto.setPayload(generalProductResponseDtos);
        return responseDto;
    }

    @Override
    public ApplicationResponseDto<?> addFavorite(Long userId, Long productId) throws NotFoundException, AlreadyExistsException{
        UserEntity user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("User not found!"));
        ProductEntity product = productRepository.findById(productId).orElseThrow(() -> new NotFoundException("Product not found!"));
        if (user.getFavouriteProducts().contains(product)) {
            throw new AlreadyExistsException("Already in favourites");
        }
        user.getFavouriteProducts().add(product);
        userRepository.save(user);
        ApplicationResponseDto<?> responseDto = new ApplicationResponseDto<>();
        responseDto.setMessage("Product add to favourites!");
        responseDto.setStatus("OK");
        responseDto.setCode(201);
        return responseDto;
    }

    @Override
    public ApplicationResponseDto<?> deleteFavorite(Long userId, Long productId) throws NotFoundException, AlreadyExistsException{
        UserEntity user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("User not found!"));
        ProductEntity product = productRepository.findById(productId).orElseThrow(() -> new NotFoundException("Product not found!"));
        if (!user.getFavouriteProducts().contains(product)) {
            throw new AlreadyExistsException("Not in favourites");
        }
        user.getFavouriteProducts().remove(product);
        userRepository.save(user);
        ApplicationResponseDto<?> responseDto = new ApplicationResponseDto<>();
        responseDto.setMessage("Product remove to favourites!");
        responseDto.setStatus("OK");
        responseDto.setCode(200);
        return responseDto;
    }
}
