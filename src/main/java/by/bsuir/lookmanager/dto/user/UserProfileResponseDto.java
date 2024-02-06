package by.bsuir.lookmanager.dto.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserProfileResponseDto {
    String firstname;
    String lastname;
    boolean sex;
    String email;
   // String countryName;
   // String cityName;
    Timestamp dateOfBirth;
}
