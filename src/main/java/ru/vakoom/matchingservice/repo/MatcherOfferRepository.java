package ru.vakoom.matchingservice.repo;

import org.springframework.data.repository.CrudRepository;
import ru.vakoom.matchingservice.model.MatcherOffer;

import java.util.Optional;

public interface MatcherOfferRepository extends CrudRepository<MatcherOffer, Long> {
    //ToDo оптимизировать для оптарвки батч матчинга
    Optional<MatcherOffer> findByNameAndShopAndBrandAndAge(String name, String shop, String brand, String age);
}
