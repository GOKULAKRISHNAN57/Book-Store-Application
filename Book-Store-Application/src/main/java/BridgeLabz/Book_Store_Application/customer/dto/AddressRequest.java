package BridgeLabz.Book_Store_Application.customer.dto;

import BridgeLabz.Book_Store_Application.enums.AddressType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AddressRequest {

    @NotBlank(message = "Address line is required.")
    @Size(max = 255, message = "Address line must not exceed 255 characters.")
    private String addressLine;

    @NotBlank(message = "City is required.")
    @Size(max = 100, message = "City must not exceed 100 characters.")
    private String city;

    @NotBlank(message = "State is required.")
    @Size(max = 100, message = "State must not exceed 100 characters.")
    private String state;

    @NotBlank(message = "Country is required.")
    @Size(max = 100, message = "Country must not exceed 100 characters.")
    private String country;

    @NotBlank(message = "Postal code is required.")
    @Pattern(
            regexp = "^[1-9][0-9]{5}$",
            message = "Postal code must be a valid 6-digit Indian PIN code."
    )
    private String postalCode;

    @NotNull(message = "Address type is required.")
    private AddressType addressType;
}