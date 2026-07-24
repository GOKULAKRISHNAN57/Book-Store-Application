package BridgeLabz.Book_Store_Application.admin.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserSummaryResponse {

    private Long userId;

    private String name;

    private String email;

    private String role;

    private Boolean active;

}