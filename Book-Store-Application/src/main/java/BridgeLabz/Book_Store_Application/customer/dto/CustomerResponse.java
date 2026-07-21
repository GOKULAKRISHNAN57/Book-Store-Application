package BridgeLabz.Book_Store_Application.customer.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomerResponse {

    private Long customerId;

    private Long userId;

    private String firstName;

    private String lastName;

    private String phoneNumber;

    private List<AddressResponse> addresses;

}