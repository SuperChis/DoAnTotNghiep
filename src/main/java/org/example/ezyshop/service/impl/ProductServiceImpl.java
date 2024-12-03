package org.example.ezyshop.service.impl;

import org.example.ezyshop.dto.pagination.PageDto;
import org.example.ezyshop.dto.product.ProductDTO;
import org.example.ezyshop.dto.product.ProductRequest;
import org.example.ezyshop.dto.product.ProductResponse;
import org.example.ezyshop.entity.Category;
import org.example.ezyshop.entity.Product;
import org.example.ezyshop.exception.NotFoundException;
import org.example.ezyshop.exception.RequetFailException;
import org.example.ezyshop.mapper.ProductMapper;
import org.example.ezyshop.repository.CartRepository;
import org.example.ezyshop.repository.CategoryRepository;
import org.example.ezyshop.repository.ProductRepository;
import org.example.ezyshop.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository repository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private CartRepository cartRepository;

//    @Autowired
//    private CartService cartService;
//
//    @Autowired
//    private FileService fileService;


//    @Value("${project.image}")
//    private String path;

    @Override
    @Transactional
    public ProductResponse createProduct(Long categoryId, ProductRequest request) {

        Category savedCategory = categoryRepository.findByIdAndIsDeletedFalse(categoryId);

        if (savedCategory == null) {
            throw new NotFoundException(false, 400, "Category not found");
        }

        Product productCheck = repository.findByNameAndCategory(request.getName(), categoryId);

        if (productCheck != null) {
            throw new RequetFailException(false, 409, "product exists");
        }

        Product product = ProductMapper.MAPPER.toModel(request);
        product.setCategory(savedCategory);

        double specialPrice = product.getPrice() - ((product.getDiscount() * 0.01) * product.getPrice());
        product.setSpecialPrice(specialPrice);

        repository.save(product);

        return new ProductResponse(true, 200).setDto(ProductMapper.MAPPER.toDTO(product));

    }

    @Override
    public ProductResponse getAllProducts(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {
        Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(pageNumber, pageSize, sortByAndOrder);

        Page<Product> pageProducts = repository.findByIsDeletedFalse(pageable);

        List<Product> products = pageProducts.getContent();

        List<ProductDTO> productDTOs = products.stream().map(ProductMapper.MAPPER::toDTO)
                .collect(Collectors.toList());

        return new ProductResponse(true, 200)
                .setDtoList(productDTOs)
                .setPageDto(PageDto.populatePageDto(pageProducts));
    }

    @Override
    public ProductResponse searchByCategory(Long categoryId, Integer pageNumber, Integer pageSize, String sortBy,
                                            String sortOrder) {

        Category savedCategory = categoryRepository.findByIdAndIsDeletedFalse(categoryId);
        if (savedCategory == null) {
            throw new NotFoundException(false, 400, "Category not found");
        }

        Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(pageNumber, pageSize, sortByAndOrder);

        Page<Product> pageProducts = repository.findByCategoryAnđIsDeletedFalse(categoryId, pageable);
        List<Product> products = pageProducts.getContent();
        List<ProductDTO> productDTOs = products.stream().map(ProductMapper.MAPPER::toDTO)
                .collect(Collectors.toList());

        return new ProductResponse(true, 200)
                .setDtoList(productDTOs)
                .setPageDto(PageDto.populatePageDto(pageProducts));
    }

    @Override
    @Transactional
    public ProductResponse updateProduct(Long productId, ProductRequest request) {
        Product productFromDB = repository.findByIdAndIsDeletedFalse(productId);

        if (productFromDB == null) {
            throw new NotFoundException(false, 404, "Not found product to update");
        }

        ProductMapper.MAPPER.updateProductFromRequest(request, productFromDB);

        if (request.getCategoryId() != null) {
            Category category = categoryRepository.findByIdAndIsDeletedFalse(request.getCategoryId());
            if (category == null) {
                throw new NotFoundException(false, 404, "category not found, can't update");
            }
            productFromDB.setCategory(category);
        }
        double specialPrice = request.getPrice() - ((request.getDiscount() * 0.01) * request.getPrice());
        productFromDB.setSpecialPrice(specialPrice);

        repository.save(productFromDB);

//        List<Cart> carts = cartRepo.findCartsByProductId(productId);
//
//        List<CartDTO> cartDTOs = carts.stream().map(cart -> {
//            CartDTO cartDTO = modelMapper.map(cart, CartDTO.class);
//
//            List<ProductDTO> products = cart.getCartItems().stream()
//                    .map(p -> modelMapper.map(p.getProduct(), ProductDTO.class)).collect(Collectors.toList());
//
//            cartDTO.setProducts(products);
//
//            return cartDTO;
//
//        }).collect(Collectors.toList());
//
//        cartDTOs.forEach(cart -> cartService.updateProductInCarts(cart.getCartId(), productId));

        return new ProductResponse(true, 200)
                .setDto(ProductMapper.MAPPER.toDTO(productFromDB));
    }

    @Override
    public ProductResponse searchProductByKeyword(String keyword, Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {
        Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageDetails = PageRequest.of(pageNumber, pageSize, sortByAndOrder);

        Page<Product> pageProducts = repository.searchByKeyword(keyword, pageDetails);

        List<Product> products = pageProducts.getContent();

        List<ProductDTO> productDTOs = products.stream().map(ProductMapper.MAPPER::toDTO)
                .collect(Collectors.toList());

        return new ProductResponse(true, 200)
                .setDtoList(productDTOs)
                .setPageDto(PageDto.populatePageDto(pageProducts));
    }


    @Override
    public ProductResponse deleteProduct(Long productId) {

        Product productInDB = repository.findByIdAndIsDeletedFalse(productId);

        if (productInDB != null) {
            throw new RequetFailException(false, 409, "product exists");
        }

        productInDB.setDeleted(true);
//        List<Cart> carts = cartRepo.findCartsByProductId(productId);
//
//        carts.forEach(cart -> cartService.deleteProductFromCart(cart.getCartId(), productId));

        repository.save(productInDB);

        return new ProductResponse(true, 200, "delete successfully");
    }
}
