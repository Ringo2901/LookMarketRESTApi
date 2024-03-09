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
public class UserLoginRequestDto {
    private String phone;
    @Email
    private String email;
    @NotBlank
    @Size(min = 4)
    private String password;
}
