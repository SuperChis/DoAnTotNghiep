package org.example.ezyshop.repository;

import org.example.ezyshop.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsernameAndIsDeletedFalse(String username);

    Optional<User> findByIdAndIsDeletedFalse(Long id);

    @Query("select u " +
            "from User u " +
            "where u.email = (?1) " +
            "   and u.isDeleted = false ")
    Optional<User> findByEmailAndIsDeletedFalse(String email);

    Boolean existsByUsername(String username);

    Boolean existsByEmail(String email);


}
