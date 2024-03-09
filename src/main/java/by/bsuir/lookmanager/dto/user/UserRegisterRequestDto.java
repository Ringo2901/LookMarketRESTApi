package by.bsuir.lookmanager.dto.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRegisterRequestDto {
    private String login;
    private String email;
    private String phone;
    private String firstname;
    private String lastname;
    private String password;
}
