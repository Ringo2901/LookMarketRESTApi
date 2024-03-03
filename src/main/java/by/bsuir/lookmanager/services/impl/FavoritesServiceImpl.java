package by.bsuir.lookmanager.services.impl;

import by.bsuir.lookmanager.dao.UserRepository;
import by.bsuir.lookmanager.dto.ApplicationResponseDto;
import by.bsuir.lookmanager.dto.product.general.GeneralProductResponseDto;
import by.bsuir.lookmanager.dto.product.general.mapper.ProductListMapper;
import by.bsuir.lookmanager.entities.user.UserEntity;
import by.bsuir.lookmanager.services.FavoritesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class FavoritesServiceImpl implements FavoritesService {
    @Autowired
    UserRepository userRepository;
    @Autowired
    ProductListMapper productListMapper;

    @Override
    public ApplicationResponseDto<List<GeneralProductResponseDto>> getFavouritesByUserId(Long id) {
        ApplicationResponseDto<List<GeneralProductResponseDto>> responseDto = new ApplicationResponseDto<>();
        UserEntity user = userRepository.findById(id).orElse(null);
        if (user == null) {
            responseDto.setMessage("User not found!");
            responseDto.setStatus("ERROR");
            responseDto.setCode(404);
        } else {
            responseDto.setMessage("User found!");
            responseDto.setStatus("OK");
            responseDto.setCode(200);
            responseDto.setPayload(productListMapper.toGeneralProductResponseDtoList(user.getFavouriteProducts()));
        }
        return responseDto;
    }
}
