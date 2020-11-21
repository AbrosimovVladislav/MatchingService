package ru.vakoom.matchingservice.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import ru.vakoom.matchingservice.model.FinalOffer;
import ru.vakoom.matchingservice.model.Product;
import ru.vakoom.matchingservice.service.aspect.logging.MeasurePerformance;

import java.util.List;

@Component
public class AggregatorClient {

    @Value("${aggregator.base-path}")
    public String AGGREGATOR_BASE_PATH;
    @Value("${aggregator.offers-path}")
    public String AGGREGATOR_OFFERS_PATH;
    @Value("${aggregator.products-path}")
    public String AGGREGATOR_PRODUCTS_PATH;

    private final RestTemplate restTemplate = new RestTemplate();

    @MeasurePerformance
    public ResponseEntity<List<FinalOffer>> sendOffersToAggregator(List<FinalOffer> finalOffers) {
        String url = AGGREGATOR_BASE_PATH + AGGREGATOR_OFFERS_PATH;
        return restTemplate.exchange(
                url,
                HttpMethod.POST,
                new HttpEntity<>(finalOffers),
                new ParameterizedTypeReference<>() {
                }
        );
    }

    @MeasurePerformance
    public List<Product> getProducts() {
        String url = AGGREGATOR_BASE_PATH + AGGREGATOR_PRODUCTS_PATH;
        return restTemplate.exchange(
                url,
                HttpMethod.GET,
                RequestEntity.EMPTY,
                new ParameterizedTypeReference<List<Product>>() {
                }
        ).getBody();
    }

}
