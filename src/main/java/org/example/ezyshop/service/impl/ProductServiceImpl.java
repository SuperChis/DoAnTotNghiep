package org.example.ezyshop.service.impl;

import org.example.ezyshop.config.service.UserDetailsImpl;
import org.example.ezyshop.dto.pagination.PageDto;
import org.example.ezyshop.dto.product.ProductDTO;
import org.example.ezyshop.dto.product.ProductRequest;
import org.example.ezyshop.dto.product.ProductResponse;
import org.example.ezyshop.dto.review.ReviewDTO;
import org.example.ezyshop.dto.variant.VariantDTO;
import org.example.ezyshop.entity.*;
import org.example.ezyshop.exception.NotFoundException;
import org.example.ezyshop.exception.RequetFailException;
import org.example.ezyshop.mapper.ProductMapper;
import org.example.ezyshop.mapper.ReviewMapper;
import org.example.ezyshop.mapper.VariantMapper;
import org.example.ezyshop.repository.*;
import org.example.ezyshop.service.AmazonClient;
import org.example.ezyshop.service.FileStorageService;
import org.example.ezyshop.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository repository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private StoreRepository storeRepository;

    @Autowired
    private FileStorageService fileService;

    @Autowired
    private VariantRepository variantRepository;

    @Autowired
    private AmazonClient amazonClient;
    @Autowired
    private ReviewRepository reviewRepository;


//    @Autowired
//    private CartService cartService;
//
//    @Autowired
//    private FileService fileService;


//    @Value("${project.image}")
//    private String path;

    @Override
    @Transactional
    public ProductResponse createProduct( ProductRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        User user = userDetails.getUser();
        StoreEntity store = storeRepository.findByUserId(user.getId());
        if (store == null) {
            throw new NotFoundException(false, 404, "Not found store");
        }

        Category savedCategory = categoryRepository.findByIdAndIsDeletedFalse(request.getCategoryId());

        if (savedCategory == null) {
            throw new NotFoundException(false, 400, "Category not found");
        }

        Product productCheck = repository.findByNameAndCategory(request.getName(), request.getCategoryId());

        if (productCheck != null) {
            throw new RequetFailException(false, 409, "product exists");
        }

        Product product = ProductMapper.MAPPER.toModel(request);
        product.setCategory(savedCategory);

        double specialPrice = product.getOriginalPrice() - ((product.getDiscount() * 0.01) * product.getOriginalPrice());
        product.setSpecialPrice(specialPrice);
        product.setDeleted(false);
        product.setStore(store);
        product.setCreated(new Date());
        product.setLastUpdate(new Date());

        String url;
        try {
            url = amazonClient.uploadFile(request.getFile());
        } catch (Exception e) {
            throw new RequetFailException("Unexpected error occurred while uploading the photo");
        }
        product.setImageURL(url);

        repository.save(product);

        return new ProductResponse(true, 200).setDto(ProductMapper.MAPPER.toDTO(product));

    }

    @Override
    public ProductResponse addThumbnailProduct(Long productId, MultipartFile file) {
        Product product = repository.findByIdAndIsDeletedFalse(productId);
        String url;
        try {
            url = fileService.storeFile(file);
        } catch (RequetFailException e) {
            throw e;
        } catch (Exception e) {
            throw new RequetFailException("Unexpected error occurred while uploading the photo");
        }
        product.setImageURL(url);
        product.setLastUpdate(new Date());
        repository.save(product);
        return new ProductResponse(true, 200, "add thumbnail successfully");
    }

    @Override
    public ProductResponse getAllProducts(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {
        Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(pageNumber, pageSize, sortByAndOrder);

        Page<Product> pageProducts = repository.findByIsDeletedFalse(pageable);

        List<Product> products = pageProducts.getContent();
        Map<Long, List<Variant>> mapVariantByProductId = products.stream().collect(Collectors.toMap(
                Product::getId,
                Product::getVariants
        ));
        List<ProductDTO> productDTOs = products.stream().map(ProductMapper.MAPPER::toDTO)
                .collect(Collectors.toList());

//        productDTOs = productDTOs.stream()
//                .peek(dto -> dto.setVariant(Optional.ofNullable(mapVariantByProductId.get(dto.getProductId()))
//                        .orElse(Collections.emptyList())))
//                .collect(Collectors.toList());

//        List<Long> productIds = products.stream().map(Product::getId).collect(Collectors.toList());
//
//        List<Variant> variants = variantRepository.findByProductIdIn(productIds);

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

        Page<Product> pageProducts = repository.findByCategoryAndIsDeletedFalse(categoryId, pageable);
        List<Product> products = pageProducts.getContent();
        List<ProductDTO> productDTOs = products.stream().map(ProductMapper.MAPPER::toDTO)
                .collect(Collectors.toList());

        return new ProductResponse(true, 200)
                .setDtoList(productDTOs)
                .setPageDto(PageDto.populatePageDto(pageProducts));
    }

    @Override
    public ProductResponse searchByStore(Long storeId, Integer pageNumber, Integer pageSize, String sortBy,
                                            String sortOrder) {
        Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(pageNumber, pageSize, sortByAndOrder);

        Page<Product> pageProducts = repository.findByStoreAndIsDeletedFalse(storeId, pageable);
        List<Product> products = pageProducts.getContent();
        List<ProductDTO> productDTOs = products.stream().map(ProductMapper.MAPPER::toDTO)
                .collect(Collectors.toList());

        return new ProductResponse(true, 200)
                .setDtoList(productDTOs)
                .setPageDto(PageDto.populatePageDto(pageProducts));
    }

    @Override
    public ProductResponse getById(Long id) {
        Product product = repository.findByIdAndIsDeletedFalse(id);
        ProductDTO dto = ProductMapper.MAPPER.toDTO(product);
        List<Review> reviews = reviewRepository.findByProductId(product.getId());
        List<ReviewDTO> reviewDTOS = reviews.stream().map(ReviewMapper.INSTANCE::toDto).toList();
        dto.setReviewDTOS(reviewDTOS);
        List<Variant> variants = variantRepository.findByProductId(product.getId());
        List<VariantDTO> variantDTOS = variants.stream().map(VariantMapper.MAPPER::toDTO).toList();
        dto.setVariantDTOS(variantDTOS);
        return new ProductResponse(true, 200).setDto(dto);
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

        if (request.getFile() != null) {
            String url;
            try {
                url = amazonClient.uploadFile(request.getFile());
            } catch (Exception e) {
                throw new RequetFailException("Unexpected error occurred while uploading the photo");
            }
            productFromDB.setImageURL(url);
        }
        double specialPrice = request.getPrice() - ((request.getDiscount() * 0.01) * request.getPrice());
        productFromDB.setSpecialPrice(specialPrice);
        productFromDB.setLastUpdate(new Date());
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
    public ProductResponse searchProduct(String keyword, Long minPrice, Long maxPrice, Long categoryId, Long storeId,
                                         Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {
        Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageDetails = PageRequest.of(pageNumber, pageSize, sortByAndOrder);

        Page<Product> pageProducts = repository.searchByKeyword(keyword, minPrice, maxPrice, categoryId, storeId, pageDetails);

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

        if (productInDB == null) {
            throw new RequetFailException(false, 409, "product not exists");
        }

        productInDB.setDeleted(true);
//        List<Cart> carts = cartRepo.findCartsByProductId(productId);
//
//        carts.forEach(cart -> cartService.deleteProductFromCart(cart.getCartId(), productId));

        repository.save(productInDB);

        return new ProductResponse(true, 200, "delete successfully");
    }
}
