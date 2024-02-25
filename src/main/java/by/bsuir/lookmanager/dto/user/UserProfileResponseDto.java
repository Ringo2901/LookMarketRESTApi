package by.bsuir.lookmanager.dto.user;

import by.bsuir.lookmanager.entities.user.information.UserProfile;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;
import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserProfileResponseDto {
    private String login;
    private String email;
    private boolean authorisationStatus;
    private Date registrationDate;
    private Timestamp lastSignIn;
    private UserProfile userProfile;
}
