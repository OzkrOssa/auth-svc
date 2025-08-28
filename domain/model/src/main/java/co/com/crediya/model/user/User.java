package co.com.crediya.model.user;

import lombok.Builder;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class User {
    private String firstName;
    private String lastName;
    private String email;
    private String document;
    private String phone;
    private Long baseSalary;
    private static final long MAX_SALARY = 15_000_000;

    public Boolean isBaseSalaryValid() {
        return this.baseSalary != null && this.baseSalary > 0 && this.baseSalary <= MAX_SALARY;
    }
}
