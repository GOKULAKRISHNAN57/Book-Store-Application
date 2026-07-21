package BridgeLabz.Book_Store_Application.auth.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponse {

    private Long id;

    private String fullName;

    private String email;

    private String role;

    private String token;

    private String tokenType;

}
