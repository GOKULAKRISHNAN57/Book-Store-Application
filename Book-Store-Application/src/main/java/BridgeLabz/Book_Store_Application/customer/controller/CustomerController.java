package BridgeLabz.Book_Store_Application.customer.controller;

import BridgeLabz.Book_Store_Application.common.response.ApiResponse;
import BridgeLabz.Book_Store_Application.customer.dto.AddressRequest;
import BridgeLabz.Book_Store_Application.customer.dto.CustomerDetailsRequest;
import BridgeLabz.Book_Store_Application.customer.dto.CustomerResponse;
import BridgeLabz.Book_Store_Application.customer.service.CustomerService;
import BridgeLabz.Book_Store_Application.security.UserDetailsImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/customer")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService customerService;

    /**
     * Create customer profile
     */
    @PostMapping("/profile")
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<CustomerResponse> createCustomerProfile(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @Valid @RequestBody CustomerDetailsRequest request) {

        CustomerResponse response = customerService.createCustomerProfile(
                userDetails.getId(),
                request
        );

        return ApiResponse.success(
                "Customer profile created successfully.",
                response
        );
    }

    /**
     * Get customer profile
     */
    @GetMapping("/profile")
    public ApiResponse<CustomerResponse> getCustomerProfile(
            @AuthenticationPrincipal UserDetailsImpl userDetails) {

        CustomerResponse response = customerService.getCustomerProfile(
                userDetails.getId()
        );

        return ApiResponse.success(
                "Customer profile retrieved successfully.",
                response
        );
    }

    /**
     * Update customer profile
     */
    @PutMapping("/profile")
    public ApiResponse<CustomerResponse> updateCustomerProfile(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @Valid @RequestBody CustomerDetailsRequest request) {

        CustomerResponse response = customerService.updateCustomerProfile(
                userDetails.getId(),
                request
        );

        return ApiResponse.success(
                "Customer profile updated successfully.",
                response
        );
    }

    /**
     * Delete customer profile
     */
    @DeleteMapping("/profile")
    public ApiResponse<Void> deleteCustomerProfile(
            @AuthenticationPrincipal UserDetailsImpl userDetails) {

        customerService.deleteCustomerProfile(userDetails.getId());

        return ApiResponse.success(
                "Customer profile deleted successfully.",
                null
        );
    }

    /**
     * Add customer address
     */
    @PostMapping("/address")
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<CustomerResponse> addAddress(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @Valid @RequestBody AddressRequest request) {

        CustomerResponse response = customerService.addAddress(
                userDetails.getId(),
                request
        );

        return ApiResponse.success(
                "Address added successfully.",
                response
        );
    }

    /**
     * Update customer address
     */
    @PutMapping("/address/{addressId}")
    public ApiResponse<CustomerResponse> updateAddress(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @PathVariable Long addressId,
            @Valid @RequestBody AddressRequest request) {

        CustomerResponse response = customerService.updateAddress(
                userDetails.getId(),
                addressId,
                request
        );

        return ApiResponse.success(
                "Address updated successfully.",
                response
        );
    }

    /**
     * Delete customer address
     */
    @DeleteMapping("/address/{addressId}")
    public ApiResponse<CustomerResponse> deleteAddress(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @PathVariable Long addressId) {

        CustomerResponse response = customerService.deleteAddress(
                userDetails.getId(),
                addressId
        );

        return ApiResponse.success(
                "Address deleted successfully.",
                response
        );
    }
}