package ru.vakoom.matchingservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ru.vakoom.matchingservice.model.FinalOffer;
import ru.vakoom.matchingservice.model.MatcherOffer;
import ru.vakoom.matchingservice.model.ScrapperOffer;
import ru.vakoom.matchingservice.repo.MatcherOfferRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MatcherService {

    public static final String AGGREGATOR_SERVICE_BASE_PATH = "http://localhost:8082";
    public static final String AGGREGATOR_SERVICE_RECEIVE_FINAL_OFFERS_PATH = "/offers";

    private final MatcherOfferRepository matcherOfferRepository;
    private final TroubleTicketService troubleTicketService;
    private final RestTemplate restTemplate = new RestTemplate();

    public void matchOffers(List<ScrapperOffer> scrapperOffers) {
        List<FinalOffer> finalOffers = new ArrayList<>();

        for (var scrapperOffer : scrapperOffers) {
            Optional<MatcherOffer> matcherOfferOrEmpty = getMatcherOfferByScrapperOffer(scrapperOffer);
            if (matcherOfferOrEmpty.isPresent()) {
                FinalOffer finalOffer = convertToFinalOffer(scrapperOffer, matcherOfferOrEmpty.get());
                finalOffers.add(finalOffer);
            } else {
                matchOfferWithProducts(scrapperOffer)
                        .ifPresentOrElse(
                                matcherOfferRepository::save,
                                () -> troubleTicketService.sendToTroubleTicket(scrapperOffer));
            }
        }
        sendOffersToAggregator(finalOffers);
    }

    private Optional<MatcherOffer> getMatcherOfferByScrapperOffer(ScrapperOffer scrapperOffer) {
        //ToDo оптимизировать для оптарвки батч матчинга
        return matcherOfferRepository.findByNameAndShop(scrapperOffer.getName(), scrapperOffer.getShopName());
    }

    private FinalOffer convertToFinalOffer(ScrapperOffer scrapperOffer, MatcherOffer matcherOffer) {
        return new FinalOffer()
                .setName(scrapperOffer.getName())
                .setBrand(scrapperOffer.getBrand())
                .setPrice(scrapperOffer.getPrice())
                .setInStore(scrapperOffer.getInStore())
                .setCategory(scrapperOffer.getCategory())
                .setShopName(scrapperOffer.getShopName())
                .setLink(scrapperOffer.getLink())
                .setProductId(matcherOffer.getProductId());

    }

    private Optional<MatcherOffer> matchOfferWithProducts(ScrapperOffer scrapperOffer) {
        //ToDo implement
        //Если смогли сматчить, вернули матчер оффер, если нет то отправили запрос в трабл тикет
        return Optional.empty();
    }

    private void sendOffersToAggregator(List<FinalOffer> finalOffers) {
        String url = AGGREGATOR_SERVICE_BASE_PATH + AGGREGATOR_SERVICE_RECEIVE_FINAL_OFFERS_PATH;
        restTemplate.exchange(url,
                HttpMethod.POST,
                new HttpEntity<>(finalOffers),
                new ParameterizedTypeReference<List<FinalOffer>>() {
                });
    }


}
