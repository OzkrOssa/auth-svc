package co.com.crediya.api.dto;

import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserRequestDto {

    @NotBlank(message = "firstName is required")
    @Size(max = 50, message = "firstName can have max 50 characters")
    private String firstName;

    @NotBlank(message = "lastName is required")
    @Size(max = 50, message = "lastName can have max 50 characters")
    private String lastName;

    private String document;

    @NotBlank(message = "email is required")
    @Email(message = "email must be valid")
    private String email;

    private String phone;

    @NotNull(message = "salary is required")
    @Min(value = 1, message = "base salary must be greater than 0")
    @Max(value = 15_000_000, message = "base salary must be less than or equal 15,000,000")
    private Long baseSalary;
}
