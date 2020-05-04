package ru.vakoom.matchingservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ru.vakoom.matchingservice.model.Product;
import ru.vakoom.matchingservice.repo.ProductRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    public static final String AGGREGATOR_BASE_PATH = "http://localhost:8082";
    public static final String AGGREGATOR_MATCHER_PRODUCTS_PATH = "/matcherProducts";
    private final ProductRepository productRepository;
    private final RestTemplate restTemplate = new RestTemplate();

    public List<Product> save(List<Product> products) {
        return productRepository.saveAll(products);
    }

    public List<Product> getProductsFromAggregator() {
        String url = AGGREGATOR_BASE_PATH + AGGREGATOR_MATCHER_PRODUCTS_PATH;
        return restTemplate.exchange(
                url,
                HttpMethod.GET,
                RequestEntity.EMPTY,
                new ParameterizedTypeReference<List<Product>>() {
                }
        ).getBody();
    }

}
