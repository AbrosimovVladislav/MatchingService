package ru.vakoom.matchingservice.model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;

@Data
@Entity
public class Product {
    @Id private Long productId;
    private String menuItem;
    private String link;
    private String model;
    private String brand;
}
