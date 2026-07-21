package BridgeLabz.Book_Store_Application.customer.repository;

import BridgeLabz.Book_Store_Application.customer.entity.Address;
import BridgeLabz.Book_Store_Application.customer.entity.CustomerProfile;
import BridgeLabz.Book_Store_Application.enums.AddressType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AddressRepository extends JpaRepository<Address, Long> {

    /**
     * Get all addresses of a customer.
     */
    List<Address> findByCustomerProfile(CustomerProfile customerProfile);

    /**
     * Get all addresses using customer profile id.
     */
    List<Address> findByCustomerProfileId(Long customerProfileId);

    /**
     * Find a specific address belonging to a customer.
     */
    Optional<Address> findByIdAndCustomerProfile(
            Long id,
            CustomerProfile customerProfile
    );

    /**
     * Delete all addresses of a customer.
     */
    void deleteByCustomerProfile(CustomerProfile customerProfile);

    /**
     * Check if customer already has a specific address type.
     */
    boolean existsByCustomerProfileAndAddressType(
            CustomerProfile customerProfile,
            AddressType addressType
    );

}