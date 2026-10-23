package com.yugal.store.products;

import com.yugal.store.users.UserMapper;
import com.yugal.store.users.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

import static java.util.Objects.nonNull;

@RestController
@AllArgsConstructor
@RequestMapping("/products")
public class ProductController {
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    private final UserMapper userMapper;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;

    @GetMapping
    public ResponseEntity<List<ProductDto>> getAllProducts(@RequestParam(required = false, defaultValue = "", name = "categoryId") Byte categoryId) {

        if (nonNull(categoryId)) {
            return ResponseEntity.ok(productRepository.findByCategoryId(categoryId).stream().map(productMapper::toDto).toList());
        }
        return ResponseEntity.ok(productRepository.findAllWithCategory().stream().map(productMapper::toDto).toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDto> getProductById(@PathVariable Long id) {
        var product = productRepository.findById(id).orElse(null);
        return nonNull(product) ? ResponseEntity.ok(productMapper.toDto(product)) : ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<ProductDto> createProduct(@RequestBody AddProductRequest request, UriComponentsBuilder uriBuilder) {
        var category = categoryRepository.findById(request.getCategoryId()).orElse(null);
        if (!nonNull(category)) {
            return ResponseEntity.badRequest().build();
        }
        var product = productMapper.toEntity(request);
        product.setCategory(category);
        var savedProduct = productRepository.save(product);
        return ResponseEntity.created(uriBuilder.path("/products/{id}").buildAndExpand(savedProduct.getId()).toUri()).body(productMapper.toDto(savedProduct));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateProduct(@PathVariable(name = "id") Long id, @RequestBody UpdateProductRequest request) {
        var product = productRepository.findById(id).orElse(null);
        if (!nonNull(product)) return ResponseEntity.notFound().build();

        var category = categoryRepository.findById(request.getCategoryId()).orElse(null);
        if (!nonNull(category)) return ResponseEntity.badRequest().build();
        product.setCategory(category);

        productRepository.save(productMapper.updateProduct(request, product));
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable(name = "id") Long id) {
        var product = productRepository.findById(id).orElse(null);
        if (!nonNull(product)) return ResponseEntity.notFound().build();
        productRepository.delete(product);
        return ResponseEntity.noContent().build();
    }
}
