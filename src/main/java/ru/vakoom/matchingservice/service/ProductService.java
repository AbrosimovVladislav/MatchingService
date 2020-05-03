package ru.vakoom.matchingservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.vakoom.matchingservice.model.Product;
import ru.vakoom.matchingservice.repo.ProductRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    public List<Product> save(List<Product> products){
        return (List<Product>) productRepository.saveAll(products);
    }

}
