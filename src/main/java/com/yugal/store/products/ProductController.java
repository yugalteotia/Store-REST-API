package com.yugal.store.products;

import com.yugal.store.users.UserMapper;
import com.yugal.store.users.UserRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

import static java.util.Objects.nonNull;

@RestController
@AllArgsConstructor
@RequestMapping("/products")
@Tag(name = "Products", description = "Product catalog management")
public class ProductController {
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    private final UserMapper userMapper;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;

    @GetMapping
    @Operation(summary = "List products", description = "Returns all products, optionally filtered by category.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Products returned",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = ProductDto.class))))
    })
    @SecurityRequirements
    public ResponseEntity<List<ProductDto>> getAllProducts(@RequestParam(required = false, defaultValue = "", name = "categoryId") Byte categoryId) {

        if (nonNull(categoryId)) {
            return ResponseEntity.ok(productRepository.findByCategoryId(categoryId).stream().map(productMapper::toDto).toList());
        }
        return ResponseEntity.ok(productRepository.findAllWithCategory().stream().map(productMapper::toDto).toList());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a product")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Product returned",
                    content = @Content(schema = @Schema(implementation = ProductDto.class))),
            @ApiResponse(responseCode = "404", description = "Product not found")
    })
    @SecurityRequirements
    public ResponseEntity<ProductDto> getProductById(@PathVariable Long id) {
        var product = productRepository.findById(id).orElse(null);
        return nonNull(product) ? ResponseEntity.ok(productMapper.toDto(product)) : ResponseEntity.notFound().build();
    }

    @PostMapping
    @Operation(summary = "Create a product")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Product created",
                    content = @Content(schema = @Schema(implementation = ProductDto.class))),
            @ApiResponse(responseCode = "400", description = "Category not found"),
            @ApiResponse(responseCode = "401", description = "Authentication required"),
            @ApiResponse(responseCode = "403", description = "Administrator role required")
    })
    @SecurityRequirement(name = "bearerAuth")
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
    @Operation(summary = "Update a product")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Product updated"),
            @ApiResponse(responseCode = "400", description = "Category not found"),
            @ApiResponse(responseCode = "404", description = "Product not found"),
            @ApiResponse(responseCode = "401", description = "Authentication required"),
            @ApiResponse(responseCode = "403", description = "Administrator role required")
    })
    @SecurityRequirement(name = "bearerAuth")
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
    @Operation(summary = "Delete a product")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Product deleted"),
            @ApiResponse(responseCode = "404", description = "Product not found"),
            @ApiResponse(responseCode = "401", description = "Authentication required"),
            @ApiResponse(responseCode = "403", description = "Administrator role required")
    })
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<Void> deleteProduct(@PathVariable(name = "id") Long id) {
        var product = productRepository.findById(id).orElse(null);
        if (!nonNull(product)) return ResponseEntity.notFound().build();
        productRepository.delete(product);
        return ResponseEntity.noContent().build();
    }
}
