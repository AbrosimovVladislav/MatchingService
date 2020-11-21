package ru.vakoom.matchingservice.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;


@Data
@NoArgsConstructor
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
