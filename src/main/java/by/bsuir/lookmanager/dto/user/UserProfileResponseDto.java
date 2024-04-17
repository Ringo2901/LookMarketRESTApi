package by.bsuir.lookmanager.dto.user;

import by.bsuir.lookmanager.dto.catalog.CatalogResponseDto;
import by.bsuir.lookmanager.entities.user.information.City;
import by.bsuir.lookmanager.entities.user.information.Country;
import by.bsuir.lookmanager.entities.user.information.UserProfile;
import by.bsuir.lookmanager.enums.UserGender;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserProfileResponseDto {
    private Long id;
    private String login;
    private String email;
    private boolean authorisationStatus;
    private Date registrationDate;
    private String lastSignIn;

    private String firstname;
    private String lastname;
    private Date dateOfBirth;
    private String address;
    private String phoneNumber;
    private UserGender gender;
    private String postalCode;
    private Integer cityId;
    private Integer countryId;

    private String imageData;
    private List<Long> catalogsIdList;

    private boolean isSubscribed;
}
