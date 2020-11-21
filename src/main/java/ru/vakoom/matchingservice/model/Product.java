package ru.vakoom.matchingservice.model;

import lombok.Data;


@Data
public class Product {
    private Long productId;
    private String model;
    private String brand;
    private Type type;
    private String age;
}
