package BridgeLabz.Book_Store_Application.customer.service;

import BridgeLabz.Book_Store_Application.customer.dto.AddressRequest;
import BridgeLabz.Book_Store_Application.customer.dto.CustomerDetailsRequest;
import BridgeLabz.Book_Store_Application.customer.dto.CustomerResponse;

public interface CustomerService {

    /**
     * Create customer profile.
     *
     * @param userId Logged-in user ID
     * @param request Customer details
     * @return Customer profile
     */
    CustomerResponse createCustomerProfile(
            Long userId,
            CustomerDetailsRequest request
    );

    /**
     * Get customer profile.
     *
     * @param userId Logged-in user ID
     * @return Customer profile
     */
    CustomerResponse getCustomerProfile(Long userId);

    /**
     * Update customer profile.
     *
     * @param userId Logged-in user ID
     * @param request Updated customer details
     * @return Updated profile
     */
    CustomerResponse updateCustomerProfile(
            Long userId,
            CustomerDetailsRequest request
    );

    /**
     * Delete customer profile.
     *
     * @param userId Logged-in user ID
     */
    void deleteCustomerProfile(Long userId);

    /**
     * Add a new address.
     *
     * @param userId Logged-in user ID
     * @param request Address details
     * @return Updated customer profile
     */
    CustomerResponse addAddress(
            Long userId,
            AddressRequest request
    );

    /**
     * Update an existing address.
     *
     * @param userId Logged-in user ID
     * @param addressId Address ID
     * @param request Updated address
     * @return Updated customer profile
     */
    CustomerResponse updateAddress(
            Long userId,
            Long addressId,
            AddressRequest request
    );

    /**
     * Delete an address.
     *
     * @param userId Logged-in user ID
     * @param addressId Address ID
     * @return Updated customer profile
     */
    CustomerResponse deleteAddress(
            Long userId,
            Long addressId
    );

}