package co.com.crediya.api.mapper;

import co.com.crediya.api.dto.UserRequestDto;
import co.com.crediya.model.user.User;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class UserMapper {
    public static User toDomain(UserRequestDto dto){
        return User.builder()
                .firstName(dto.getFirstName())
                .lastName(dto.getLastName())
                .document(dto.getDocument())
                .email(dto.getEmail())
                .phone(dto.getPhone())
                .baseSalary(dto.getBaseSalary())
                .roleId(dto.getRoleId())
                .password(dto.getPassword())
                .build();
    }
}
