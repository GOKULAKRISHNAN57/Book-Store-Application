package BridgeLabz.Book_Store_Application.user.dto;


import BridgeLabz.Book_Store_Application.enums.Role;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {

    private Long id;

    private String fullName;

    private String email;

    private String phoneNumber;

    private Role role;

    private Boolean active;

}