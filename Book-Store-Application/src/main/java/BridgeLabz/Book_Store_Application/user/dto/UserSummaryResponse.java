package BridgeLabz.Book_Store_Application.user.dto;


import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserSummaryResponse {

    private Long id;

    private String fullName;

    private String email;

}
