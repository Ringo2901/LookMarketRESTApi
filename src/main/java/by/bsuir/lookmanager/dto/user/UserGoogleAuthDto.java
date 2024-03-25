package by.bsuir.lookmanager.dto.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserGoogleAuthDto {
    private String email;
    private String firstname;
    private String lastname;
}
