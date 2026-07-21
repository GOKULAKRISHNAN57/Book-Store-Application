package BridgeLabz.Book_Store_Application.customer.dto;

import BridgeLabz.Book_Store_Application.enums.AddressType;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AddressResponse {

    private Long id;

    private String addressLine;

    private String city;

    private String state;

    private String country;

    private String postalCode;

    private AddressType addressType;
}