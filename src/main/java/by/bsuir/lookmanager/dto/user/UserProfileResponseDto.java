package by.bsuir.lookmanager.dto.user;

import by.bsuir.lookmanager.entities.user.information.UserGender;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserProfileResponseDto {
    private Long id;
    private String login;
    private String email;
    private boolean authorisationStatus;
    private String registrationDate;
    private String lastSignIn;

    private String firstname;
    private String lastname;
    private String dateOfBirth;
    private String address;
    private String phoneNumber;
    private String gender;
    private String postalCode;
    private Integer cityId;
    private Integer countryId;
    private Float averageAssessment;

    private String userImageUrl;
    private List<Long> catalogsIdList;

    private boolean isSubscribed;
}
