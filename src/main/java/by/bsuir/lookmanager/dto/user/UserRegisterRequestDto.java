package by.bsuir.lookmanager.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRegisterRequestDto {
    @NotBlank
    private String login;
    @Email
    private String email;
    private String phone;
    @NotBlank
    private String firstname;
    @NotBlank
    private String lastname;
    @NotBlank
    @Size(min = 4)
    private String password;
}
