package org.example.ezyshop.repository;

import org.example.ezyshop.entity.Cart;
import org.example.ezyshop.entity.CartItem;
import org.example.ezyshop.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {

    @Query("SELECT ci.product FROM CartItem ci WHERE ci.product.id = ?1")
    Product findProductById(Long productId);


    @Query("SELECT ci " +
            "FROM CartItem ci " +
            "WHERE ci.cart.id = (?1) " +
            "   AND ci.product.id = (?2) " +
            "   AND ci.sizeId = (?3) " +
            "   AND ci.isDeleted = false ")
    CartItem findByProductIdAndCartId(Long cartId, Long productId, Long sizeId);

    @Query("SELECT ci " +
            "FROM CartItem ci " +
            "WHERE ci.cart.id = ?1 " +
            "   AND ci.sizeId = ?2 " +
            "   AND ci.isDeleted = false ")
    CartItem findBySizeIdAndCartId(Long cartId, Long sizeId);

    @Modifying
    @Query("DELETE FROM CartItem ci WHERE ci.cart.id = ?1 AND ci.product.id = ?2")
    void deleteCartItemByProductIdAndCartId(Long productId, Long cartId);

    @Query("SELECT ci " +
            "FROM CartItem ci " +
            "LEFT JOIN Cart c ON ci.cart.id = c.id " +
            "WHERE c.user.id = ?1 " +
            "   AND c.isDeleted = false " +
            "   AND ci.isDeleted = false ")
    List<CartItem> findByUserId(Long userId);

    @Query("SELECT ci " +
            "FROM CartItem ci " +
            "LEFT JOIN Cart c ON ci.cart.id = c.id " +
            "WHERE c.id = ?1 " +
            "   AND c.isDeleted = false " +
            "   AND ci.isDeleted = false ")
    List<CartItem> findByCartId(Long cartId);

    @Query("SELECT ci " +
            "FROM CartItem ci " +
            "LEFT JOIN Cart c ON ci.cart.id = c.id " +
            "WHERE ci.id = ?1 " +
            "   AND c.isDeleted = false " +
            "   AND ci.isDeleted = false ")
    CartItem findByCartItemId(Long cartItemId);
}
