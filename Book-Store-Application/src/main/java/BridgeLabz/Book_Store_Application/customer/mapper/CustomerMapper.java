package BridgeLabz.Book_Store_Application.customer.mapper;

import BridgeLabz.Book_Store_Application.customer.dto.AddressResponse;
import BridgeLabz.Book_Store_Application.customer.dto.CustomerResponse;
import BridgeLabz.Book_Store_Application.customer.entity.Address;
import BridgeLabz.Book_Store_Application.customer.entity.CustomerProfile;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class CustomerMapper {

    /**
     * Convert CustomerProfile entity to CustomerResponse DTO.
     */
    public CustomerResponse toResponse(CustomerProfile customerProfile) {

        List<AddressResponse> addresses = customerProfile.getAddresses()
                .stream()
                .map(this::toAddressResponse)
                .collect(Collectors.toList());

        return CustomerResponse.builder()
                .customerId(customerProfile.getId())
                .userId(customerProfile.getUser().getId())
                .firstName(customerProfile.getFirstName())
                .lastName(customerProfile.getLastName())
                .phoneNumber(customerProfile.getPhoneNumber())
                .addresses(addresses)
                .build();
    }

    /**
     * Convert Address entity to AddressResponse DTO.
     */
    public AddressResponse toAddressResponse(Address address) {

        return AddressResponse.builder()
                .id(address.getId())
                .addressLine(address.getAddressLine())
                .city(address.getCity())
                .state(address.getState())
                .country(address.getCountry())
                .postalCode(address.getPostalCode())
                .addressType(address.getAddressType())
                .build();
    }

}