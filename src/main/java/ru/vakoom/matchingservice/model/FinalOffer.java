package ru.vakoom.matchingservice.model;

import lombok.Data;
import lombok.experimental.Accessors;


@Data
@Accessors(chain = true)
public class FinalOffer {
    private String name;
    private String brand;
    private Double price;
    private Boolean inStore;
    private String category;
    private String shopName;
    private String link;
    private Long productId;
}
