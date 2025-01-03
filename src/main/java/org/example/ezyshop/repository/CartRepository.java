package org.example.ezyshop.repository;

import org.example.ezyshop.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {

    @Query("SELECT c " +
            "FROM Cart c " +
            "WHERE c.user.email = ?1 AND c.id = ?2")
    Cart findCartByEmailAndCartId(String email, Long cartId);

    @Query("SELECT c " +
            "FROM Cart c " +
            "WHERE c.user.id = ?1 AND c.isDeleted = false ")
    Cart findByUserId(Long userId);

    Cart findByIdAndIsDeletedFalse(Long id);

}
