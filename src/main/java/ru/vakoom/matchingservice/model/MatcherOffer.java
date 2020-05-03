package ru.vakoom.matchingservice.model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;

@Data
@Entity
public class MatcherOffer {

    @Id private String id;
}
