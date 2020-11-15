package ru.vakoom.matchingservice.service;

import org.springframework.http.ResponseEntity;
import ru.vakoom.matchingservice.model.FinalOffer;
import ru.vakoom.matchingservice.model.MatcherOffer;
import ru.vakoom.matchingservice.model.ScrapperOffer;

import java.util.List;

public interface MatcherService {

    ResponseEntity<List<FinalOffer>> matchOffers(List<ScrapperOffer> scrapperOffers);

    MatcherOffer save(MatcherOffer matcherOffer);

}
