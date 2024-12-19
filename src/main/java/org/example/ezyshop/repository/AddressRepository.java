package org.example.ezyshop.repository;

import org.example.ezyshop.entity.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AddressRepository extends JpaRepository<Address, Long> {
    @Query("SELECT a " +
            "from Address a " +
            "left join User u on a.user.id = u.id " +
            "where u.isDeleted = false " +
            "and a.isDeleted = false " +
            "and a.id = (?1) ")
    Optional<Address> findById(Long id);

    @Query("SELECT a " +
            "from Address a " +
            "left join User u on a.user.id = u.id " +
            "where u.isDeleted = false " +
            "and a.isDeleted = false " +
            "and u.id = (?1) ")
    List<Address> findByUserId(Long userId);

    @Query("SELECT a " +
            "from Address a " +
            "left join User u on a.user.id = u.id " +
            "where u.isDeleted = false " +
            "and a.isDeleted = false ")
    Address findByIdAndIsDeletedFalse(Long id);

    @Query("SELECT a " +
            "from Address a " +
            "left join User u on a.user.id = u.id " +
            "where u.isDeleted = false " +
            "and a.isDeleted = false " +
            "and a.id = (?1) " +
            "and a.defaultAddress = false")
    Optional<Address> findByIdAndIsDefaultAddressFalse(Long id);
}
