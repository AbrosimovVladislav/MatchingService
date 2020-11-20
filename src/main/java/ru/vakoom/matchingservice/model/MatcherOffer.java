package ru.vakoom.matchingservice.model;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.persistence.*;

import static ru.vakoom.matchingservice.model.Type.TYPE_ID;


@Data
@Entity
@Accessors(chain = true)
public class MatcherOffer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private Long productId;
    private String shop;
    private String brand;
    private String age;
    private String link;
    @ManyToOne
    @JoinColumn(name = TYPE_ID, nullable = false)
    private Type type;
}
