package clear.solutions.demo.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserModificationRequest {
    @Email
    @NotNull
    private String email;
    @NotBlank
    private String firstName;
    @NotBlank
    private String lastName;
    @Past
    private LocalDate birthDate;
    private String address;
    private String phone;
}
