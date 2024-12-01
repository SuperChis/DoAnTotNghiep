package org.example.ezyshop.service.impl;

import org.example.ezyshop.dto.product.ProductRequest;
import org.example.ezyshop.dto.product.ProductResponse;
import org.example.ezyshop.entity.Category;
import org.example.ezyshop.entity.Product;
import org.example.ezyshop.exception.NotFoundException;
import org.example.ezyshop.exception.RequetFailException;
import org.example.ezyshop.repository.CartRepository;
import org.example.ezyshop.repository.CategoryRepository;
import org.example.ezyshop.repository.ProductRepository;
import org.example.ezyshop.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

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


    @Value("${project.image}")
    private String path;

    @Override
    public ProductResponse addProduct(Long categoryId, ProductRequest request) {

        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new NotFoundException(false, 404, "category not found"));

        Product productCheck = repository.findByNameAndCategory(request.getName(), categoryId);

        if (productCheck != null) {
            throw new RequetFailException(false, 409, "product exists");
        }

        product.setImage("default.png");

        product.setCategory(category);

        double specialPrice = product.getPrice() - ((product.getDiscount() * 0.01) * product.getPrice());
        product.setSpecialPrice(specialPrice);

        Product savedProduct = repository.save(product);

        return modelMapper.map(saveProduct, ProductDTO.class);

    }

    @Override
    public ProductResponse getAllProducts(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {

        Pageable pageDetails = PageRequest.of(pageNumber, pageSize, sortByAndOrder);

        Page<Product> pageProducts = repository.findAll(pageDetails);

        List<Product> products = pageProducts.getContent();

        List<ProductDTO> productDTOs = products.stream().map(product -> modelMapper.map(product, ProductDTO.class))
                .collect(Collectors.toList());

        ProductResponse productResponse = new ProductResponse();

        productResponse.setContent(productDTOs);
        productResponse.setPageNumber(pageProducts.getNumber());
        productResponse.setPageSize(pageProducts.getSize());
        productResponse.setTotalElements(pageProducts.getTotalElements());
        productResponse.setTotalPages(pageProducts.getTotalPages());
        productResponse.setLastPage(pageProducts.isLast());

        return productResponse;
    }

    @Override
    public ProductResponse searchByCategory(Long categoryId, Integer pageNumber, Integer pageSize, String sortBy,
                                            String sortOrder) {

        Category category = categoryRepo.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "categoryId", categoryId));

        Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageDetails = PageRequest.of(pageNumber, pageSize, sortByAndOrder);

        Page<Product> pageProducts = repository.findAll(pageDetails);

        List<Product> products = pageProducts.getContent();

        if (products.size() == 0) {
            throw new APIException(category.getCategoryName() + " category doesn't contain any products !!!");
        }

        List<ProductDTO> productDTOs = products.stream().map(p -> modelMapper.map(p, ProductDTO.class))
                .collect(Collectors.toList());

        ProductResponse productResponse = new ProductResponse();

        productResponse.setContent(productDTOs);
        productResponse.setPageNumber(pageProducts.getNumber());
        productResponse.setPageSize(pageProducts.getSize());
        productResponse.setTotalElements(pageProducts.getTotalElements());
        productResponse.setTotalPages(pageProducts.getTotalPages());
        productResponse.setLastPage(pageProducts.isLast());

        return productResponse;
    }

    @Override
    public ProductResponse searchProductByKeyword(String keyword, Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {
        Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageDetails = PageRequest.of(pageNumber, pageSize, sortByAndOrder);

        Page<Product> pageProducts = repository.findByProductNameLike(keyword, pageDetails);

        List<Product> products = pageProducts.getContent();

        if (products.size() == 0) {
            throw new APIException("Products not found with keyword: " + keyword);
        }

        List<ProductDTO> productDTOs = products.stream().map(p -> modelMapper.map(p, ProductDTO.class))
                .collect(Collectors.toList());

        ProductResponse productResponse = new ProductResponse();

        productResponse.setContent(productDTOs);
        productResponse.setPageNumber(pageProducts.getNumber());
        productResponse.setPageSize(pageProducts.getSize());
        productResponse.setTotalElements(pageProducts.getTotalElements());
        productResponse.setTotalPages(pageProducts.getTotalPages());
        productResponse.setLastPage(pageProducts.isLast());

        return productResponse;
    }

    @Override
    public ProductDTO updateProduct(Long productId, Product product) {
        Product productFromDB = repository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "productId", productId));

        if (productFromDB == null) {
            throw new APIException("Product not found with productId: " + productId);
        }

        product.setImage(productFromDB.getImage());
        product.setProductId(productId);
        product.setCategory(productFromDB.getCategory());

        double specialPrice = product.getPrice() - ((product.getDiscount() * 0.01) * product.getPrice());
        product.setSpecialPrice(specialPrice);

        Product savedProduct = repository.save(product);

        List<Cart> carts = cartRepo.findCartsByProductId(productId);

        List<CartDTO> cartDTOs = carts.stream().map(cart -> {
            CartDTO cartDTO = modelMapper.map(cart, CartDTO.class);

            List<ProductDTO> products = cart.getCartItems().stream()
                    .map(p -> modelMapper.map(p.getProduct(), ProductDTO.class)).collect(Collectors.toList());

            cartDTO.setProducts(products);

            return cartDTO;

        }).collect(Collectors.toList());

        cartDTOs.forEach(cart -> cartService.updateProductInCarts(cart.getCartId(), productId));

        return modelMapper.map(savedProduct, ProductDTO.class);
    }

    @Override
    public ProductDTO updateProductImage(Long productId, MultipartFile image) throws IOException {
        Product productFromDB = repository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "productId", productId));

        if (productFromDB == null) {
            throw new APIException("Product not found with productId: " + productId);
        }

        String fileName = fileService.uploadImage(path, image);

        productFromDB.setImage(fileName);

        Product updatedProduct = repository.save(productFromDB);

        return modelMapper.map(updatedProduct, ProductDTO.class);
    }

    @Override
    public String deleteProduct(Long productId) {

        Product product = repository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "productId", productId));

        List<Cart> carts = cartRepo.findCartsByProductId(productId);

        carts.forEach(cart -> cartService.deleteProductFromCart(cart.getCartId(), productId));

        repository.delete(product);

        return "Product with productId: " + productId + " deleted successfully !!!";
    }
}
