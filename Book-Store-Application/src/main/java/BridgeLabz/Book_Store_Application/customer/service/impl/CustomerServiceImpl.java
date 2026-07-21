package BridgeLabz.Book_Store_Application.customer.service.impl;

import BridgeLabz.Book_Store_Application.customer.dto.AddressRequest;
import BridgeLabz.Book_Store_Application.customer.dto.CustomerDetailsRequest;
import BridgeLabz.Book_Store_Application.customer.dto.CustomerResponse;
import BridgeLabz.Book_Store_Application.customer.entity.Address;
import BridgeLabz.Book_Store_Application.customer.entity.CustomerProfile;
import BridgeLabz.Book_Store_Application.customer.mapper.CustomerMapper;
import BridgeLabz.Book_Store_Application.customer.repository.AddressRepository;
import BridgeLabz.Book_Store_Application.customer.repository.CustomerProfileRepository;
import BridgeLabz.Book_Store_Application.customer.service.CustomerService;
import BridgeLabz.Book_Store_Application.enums.AddressType;
import BridgeLabz.Book_Store_Application.exception.BadRequestException;
import BridgeLabz.Book_Store_Application.exception.ResourceNotFoundException;
import BridgeLabz.Book_Store_Application.user.entity.User;
import BridgeLabz.Book_Store_Application.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class CustomerServiceImpl implements CustomerService {

    private final CustomerProfileRepository customerProfileRepository;

    private final AddressRepository addressRepository;

    private final UserRepository userRepository;

    private final CustomerMapper customerMapper;

    private CustomerProfile getCustomerProfileEntity(Long userId) {

        return customerProfileRepository.findByUserId(userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Customer profile not found."
                        ));
    }

    private User getUser(Long userId) {

        return userRepository.findById(userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User not found with ID: " + userId
                        ));
    }
    @Override
    public CustomerResponse createCustomerProfile(
            Long userId,
            CustomerDetailsRequest request) {

        // Find logged-in user
        User user = getUser(userId);

        // Check if profile already exists
        if (customerProfileRepository.existsByUser(user)) {
            throw new BadRequestException("Customer profile already exists.");
        }

        // Create customer profile
        CustomerProfile customerProfile = CustomerProfile.builder()
                .user(user)
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .phoneNumber(request.getPhoneNumber())
                .build();

        // Save profile
        CustomerProfile savedCustomer =
                customerProfileRepository.save(customerProfile);

        // Convert entity to response
        return customerMapper.toResponse(savedCustomer);
    }
    @Override
    @Transactional(readOnly = true)
    public CustomerResponse getCustomerProfile(Long userId) {

        // Get customer profile
        CustomerProfile customerProfile = getCustomerProfileEntity(userId);

        // Convert entity to response DTO
        return customerMapper.toResponse(customerProfile);
    }


    @Override
    public CustomerResponse updateCustomerProfile(
            Long userId,
            CustomerDetailsRequest request) {

        // Get existing customer profile
        CustomerProfile customerProfile = getCustomerProfileEntity(userId);

        // Update profile details
        customerProfile.setFirstName(request.getFirstName());
        customerProfile.setLastName(request.getLastName());
        customerProfile.setPhoneNumber(request.getPhoneNumber());

        // Save updated profile
        CustomerProfile updatedProfile =
                customerProfileRepository.save(customerProfile);

        // Convert entity to response DTO
        return customerMapper.toResponse(updatedProfile);
    }
    @Override
    public void deleteCustomerProfile(Long userId) {

        // Get existing customer profile
        CustomerProfile customerProfile = getCustomerProfileEntity(userId);

        // Delete customer profile
        customerProfileRepository.delete(customerProfile);
    }
    @Override
    public CustomerResponse addAddress(
            Long userId,
            AddressRequest request) {

        // Get customer profile
        CustomerProfile customerProfile = getCustomerProfileEntity(userId);

        // Business rule:
        // Only one HOME and one OFFICE address are allowed
        if (request.getAddressType() != AddressType.OTHER &&
                addressRepository.existsByCustomerProfileAndAddressType(
                        customerProfile,
                        request.getAddressType())) {

            throw new BadRequestException(
                    request.getAddressType() + " address already exists."
            );
        }

        // Create address
        Address address = Address.builder()
                .customerProfile(customerProfile)
                .addressLine(request.getAddressLine())
                .city(request.getCity())
                .state(request.getState())
                .country(request.getCountry())
                .postalCode(request.getPostalCode())
                .addressType(request.getAddressType())
                .build();

        // Save address
        addressRepository.save(address);

        // Return updated customer profile
        return customerMapper.toResponse(getCustomerProfileEntity(userId));
    }
    @Override
    public CustomerResponse updateAddress(
            Long userId,
            Long addressId,
            AddressRequest request) {

        // Get customer profile
        CustomerProfile customerProfile = getCustomerProfileEntity(userId);

        // Get address belonging to this customer
        Address address = addressRepository
                .findByIdAndCustomerProfile(addressId, customerProfile)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Address not found with ID: " + addressId
                        ));

        // Validate address type change
        if (address.getAddressType() != request.getAddressType()
                && request.getAddressType() != AddressType.OTHER
                && addressRepository.existsByCustomerProfileAndAddressType(
                customerProfile,
                request.getAddressType())) {

            throw new BadRequestException(
                    request.getAddressType() + " address already exists."
            );
        }

        // Update address fields
        address.setAddressLine(request.getAddressLine());
        address.setCity(request.getCity());
        address.setState(request.getState());
        address.setCountry(request.getCountry());
        address.setPostalCode(request.getPostalCode());
        address.setAddressType(request.getAddressType());

        // Save updated address
        addressRepository.save(address);

        // Return updated customer profile
        return customerMapper.toResponse(getCustomerProfileEntity(userId));
    }
    @Override
    public CustomerResponse deleteAddress(
            Long userId,
            Long addressId) {

        // Get customer profile
        CustomerProfile customerProfile = getCustomerProfileEntity(userId);

        // Get address belonging to this customer
        Address address = addressRepository
                .findByIdAndCustomerProfile(addressId, customerProfile)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Address not found with ID: " + addressId
                        ));

        // Delete address
        addressRepository.delete(address);

        // Return updated customer profile
        return customerMapper.toResponse(getCustomerProfileEntity(userId));
    }
}