package ru.vakoom.matchingservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ru.vakoom.matchingservice.model.FinalOffer;
import ru.vakoom.matchingservice.model.MatcherOffer;
import ru.vakoom.matchingservice.model.Product;
import ru.vakoom.matchingservice.model.ScrapperOffer;
import ru.vakoom.matchingservice.repo.MatcherOfferRepository;
import ru.vakoom.matchingservice.repo.ProductRepository;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MatcherService implements InitializingBean {

    public static final String AGGREGATOR_SERVICE_BASE_PATH = "http://localhost:8082";
    public static final String AGGREGATOR_SERVICE_RECEIVE_FINAL_OFFERS_PATH = "/offers";

    private final MatcherOfferRepository matcherOfferRepository;
    private final ProductRepository productRepository;
    private final TroubleTicketService troubleTicketService;
    private final RestTemplate restTemplate = new RestTemplate();

    private List<Product> products;

    public void afterPropertiesSet() {
        products = productRepository.findAll();
    }

    public void matchOffers(List<ScrapperOffer> scrapperOffers) {
        List<FinalOffer> finalOffers = new ArrayList<>();

        for (var scrapperOffer : scrapperOffers) {
            Optional<MatcherOffer> matcherOfferOrEmpty = getMatcherOfferByScrapperOffer(scrapperOffer);
            if (matcherOfferOrEmpty.isPresent()) {
                FinalOffer finalOffer = convertToFinalOffer(scrapperOffer, matcherOfferOrEmpty.get());
                finalOffers.add(finalOffer);
            } else {
                matchOfferWithProducts(scrapperOffer)
                        .ifPresent(matcherOfferRepository::save);
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
                .setCategory(scrapperOffer.getMenuItem())
                .setShopName(scrapperOffer.getShopName())
                .setLink(scrapperOffer.getLink())
                .setProductId(matcherOffer.getProductId());

    }

    private Optional<MatcherOffer> matchOfferWithProducts(ScrapperOffer scrapperOffer) {
        List<Product> matchedProducts = products.stream()
                .filter(product -> product.getMenuItem().equals(scrapperOffer.getMenuItem()))
                .filter(product -> product.getBrand().equals(scrapperOffer.getBrand()) || scrapperOffer.getBrand().isBlank())
                .filter(product -> match(product, scrapperOffer))
                .collect(Collectors.toList());

        if (matchedProducts.size() == 1) {
            return Optional.of(createMatcherOffer(scrapperOffer, matchedProducts.get(0)));
        } else {
            troubleTicketService.sendToTroubleTicket(scrapperOffer, matchedProducts);
            return Optional.empty();
        }
    }

    private Boolean match(Product product, ScrapperOffer scrapperOffer) {
        String productName = product.getModel().toLowerCase();
        String offerName = scrapperOffer.getName().toLowerCase();
        //ToDo удалять меню айтем
        //ToDo добавить работу с возрастом

        offerName = offerName.replace(scrapperOffer.getBrand(),"");

        List<String> productNameArr = Arrays.asList(productName.split(" "));
        List<String> offerNameArr = Arrays.asList(offerName.split(" "));
        if (productNameArr.size() != offerNameArr.size()) return false;
        Collections.sort(productNameArr);
        Collections.sort(offerNameArr);
        return Objects.equals(productNameArr, offerNameArr);
    }

    private MatcherOffer createMatcherOffer(ScrapperOffer scrapperOffer, Product product) {
        return new MatcherOffer()
                .setBrand(scrapperOffer.getBrand())
                .setName(scrapperOffer.getName())
                .setProductId(product.getProductId())
                .setShop(scrapperOffer.getShopName());
    }

    private void sendOffersToAggregator(List<FinalOffer> finalOffers) {
        String url = AGGREGATOR_SERVICE_BASE_PATH + AGGREGATOR_SERVICE_RECEIVE_FINAL_OFFERS_PATH;
        restTemplate.exchange(
                url,
                HttpMethod.POST,
                new HttpEntity<>(finalOffers),
                new ParameterizedTypeReference<List<FinalOffer>>() {}
        );
    }
}
