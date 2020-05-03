package ru.vakoom.matchingservice.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ru.vakoom.matchingservice.model.Product;

import java.util.List;

@Slf4j
@Service
public class AggregatorInfoService {

    public static final String AGGREGATOR_BASE_PATH = "http://localhost:8082";
    public static final String AGGREGATOR_MATCHER_PRODUCTS_PATH = "/matcherProducts";
    private final RestTemplate restTemplate = new RestTemplate();

    public List<Product> getAll() {
        String url = AGGREGATOR_BASE_PATH + AGGREGATOR_MATCHER_PRODUCTS_PATH;
        List<Product> products = restTemplate.exchange(
                url,
                HttpMethod.GET,
                RequestEntity.EMPTY,
                new ParameterizedTypeReference<List<Product>>() {}
        ).getBody();
        return products;
    }
}
