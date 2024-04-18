package by.bsuir.lookmanager.dto.user;

import by.bsuir.lookmanager.enums.UserGender;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserProfileRequestDto {
    private String firstname;
    private String lastname;
    private String dateOfBirth;
    private String address;
    private String phoneNumber;
    private UserGender gender;
    private String postalCode;
    private Long cityId;
    private Long countryId;

    private String imageData;
}
