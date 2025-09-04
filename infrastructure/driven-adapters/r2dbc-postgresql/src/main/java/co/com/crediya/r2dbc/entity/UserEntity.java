package co.com.crediya.r2dbc.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Getter
@Setter
@RequiredArgsConstructor
@Table("users")
public class UserEntity {
    @Id
    private Long id;
    @Column("first_name")
    private String firstName;
    @Column("last_name")
    private String lastName;
    private String email;
    private String document;
    private String phone;
    @Column("base_salary")
    private Long baseSalary;
    @Column("role_id")
    private Long roleId;
    private String password;
}

