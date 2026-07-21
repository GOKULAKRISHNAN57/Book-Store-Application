package BridgeLabz.Book_Store_Application.auth.mapper;


import BridgeLabz.Book_Store_Application .auth.dto.LoginResponse;
import BridgeLabz.Book_Store_Application.auth.dto.RegisterRequest;
import BridgeLabz.Book_Store_Application .enums.Role;
import BridgeLabz.Book_Store_Application.user.entity.User;
import org.springframework.stereotype.Component;

@Component
public class AuthMapper {

    public User toEntity(RegisterRequest request) {

        return User.builder()
                .fullName(request.getFullName())
                .email(request.getEmail())
                .phoneNumber(request.getPhoneNumber())
                .password(request.getPassword())
                .role(Role.ROLE_USER)
                .active(true)
                .build();
    }

    public LoginResponse toLoginResponse(User user,
                                         String token) {

        return LoginResponse.builder()
                .id(user.getId())
                .fullName(user.getFullName())
                .email(user.getEmail())
                .role(user.getRole().name())
                .token(token)
                .tokenType("Bearer")
                .build();
    }

}
