package ru.vakoom.matchingservice.model;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;


@Data
public class Ticket {
    ScrapperOffer scrapperOffer;
    String productIds;
    LocalDateTime createdTime;

    public Ticket(ScrapperOffer scrapperOffer, List<Product> productIds, LocalDateTime createdTime) {
        this.scrapperOffer = scrapperOffer;
        this.productIds = productIds.stream().map(Product::getProductId).map(Object::toString).collect(Collectors.joining(","));
        this.createdTime = createdTime;
    }
}
