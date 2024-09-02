package by.bsuir.lookmanager.dto.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserProfileRequestDto {
    private String firstname;
    private String lastname;
    private String login;
    private String dateOfBirth;
    private String address;
    private String phoneNumber;
    private Integer genderId;
    private String postalCode;
    private Long cityId;
    private Long countryId;

    private String imageData;
}
