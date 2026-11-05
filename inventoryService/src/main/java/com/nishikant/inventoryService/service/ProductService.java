package com.nishikant.inventoryService.service;

import com.nishikant.inventoryService.dto.OrderRequestDto;
import com.nishikant.inventoryService.dto.OrderRequestItemDto;
import com.nishikant.inventoryService.dto.ProductDto;
import com.nishikant.inventoryService.entity.Product;
import com.nishikant.inventoryService.repository.ProductRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final ModelMapper modelMapper;

    public List<ProductDto> getAllInventory(){
        log.info("Fetching all inventory items");
        List<Product> inventories = productRepository.findAll();

        return inventories.stream()
                .map(product -> modelMapper.map(product, ProductDto.class))
                .toList();
    }

    public ProductDto getProductById(Long id){
        log.info("Fetching Product with Id: {}", id);
        Optional<Product> inventory = productRepository.findById(id);

        return inventory.map(item-> modelMapper.map(item, ProductDto.class))
                .orElseThrow(() -> new RuntimeException("Inventory not found"));
    }

    @Transactional
    public Double reduceStocks(OrderRequestDto orderRequestDto) {
        log.info("Reducing the stocks");
        Double totalPrice = 0.0;
        for(OrderRequestItemDto orderRequestItemDto: orderRequestDto.getItems()){
            Long productId = orderRequestItemDto.getProductId();
            Integer quantity = orderRequestItemDto.getQuantity();

            Product product = productRepository.findById(productId).orElseThrow( () ->
                    new RuntimeException("Product not found with Id: "+productId));

            if(product.getStock() < quantity){
                throw new RuntimeException("Product cannot be fulfilled for given quantity");
            }

            product.setStock(product.getStock()-quantity);
            productRepository.save(product);
            totalPrice += quantity*product.getPrice();
        }

        return totalPrice;
    }
}
