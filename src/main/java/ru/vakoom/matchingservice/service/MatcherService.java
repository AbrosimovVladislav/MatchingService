package ru.vakoom.matchingservice.service;

import lombok.RequiredArgsConstructor;
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
public class MatcherService {

    public static final String AGGREGATOR_SERVICE_BASE_PATH = "http://localhost:8082";
    public static final String AGGREGATOR_SERVICE_RECEIVE_FINAL_OFFERS_PATH = "/offers";

    private final MatcherOfferRepository matcherOfferRepository;
    private final ProductRepository productRepository;
    private final TroubleTicketService troubleTicketService;
    private final RestTemplate restTemplate = new RestTemplate();

    private List<Product> products;

    public void matchOffers(List<ScrapperOffer> scrapperOffers) {
        products = productRepository.findAll();
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
        return matcherOfferRepository.findByNameAndShopAndBrandAndAge(scrapperOffer.getName(),
                scrapperOffer.getShopName(),
                scrapperOffer.getBrand(),
                scrapperOffer.getAge());
    }

    private FinalOffer convertToFinalOffer(ScrapperOffer scrapperOffer, MatcherOffer matcherOffer) {
        return new FinalOffer()
                .setName(scrapperOffer.getName())
                .setBrand(scrapperOffer.getBrand())
                .setPrice(scrapperOffer.getPrice())
                .setInStore(scrapperOffer.getInStore())
                .setType(scrapperOffer.getType())
                .setShopName(scrapperOffer.getShopName())
                .setLink(scrapperOffer.getLink())
                .setProductId(matcherOffer.getProductId());

    }

    private Optional<MatcherOffer> matchOfferWithProducts(ScrapperOffer scrapperOffer) {
        List<Product> matchedProducts = products.stream()
                .filter(product -> product.getType().equals(scrapperOffer.getType()))
                .filter(product -> product.getBrand().equals(scrapperOffer.getBrand()) || scrapperOffer.getBrand().isBlank())
                .filter(product -> product.getAge().equals(scrapperOffer.getAge()) || scrapperOffer.getAge().isBlank())
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
        productName = productName.replace(product.getBrand().toLowerCase(), "");
        productName = productName.replace(product.getAge().toLowerCase(), "");
        String offerName = scrapperOffer.getName().toLowerCase();
        offerName = offerName.replace(scrapperOffer.getBrand().toLowerCase(), "");
        offerName = offerName.replace(scrapperOffer.getAge().toLowerCase(), "");

        //ToDo УДАЛЕНИЕ ТИПА ЧЕРЕЗ УДАЛЕНИЕ РУССКИХ СИМВОЛОВ (ПЛОХОЙ ВАРИАНТ)
        productName = productName.replaceAll("[^\\w ]", "").trim();
        offerName = offerName.replaceAll("[^\\w ]", "").trim();
        //ToDo УДАЛЕНИЕ ТИПА ЧЕРЕЗ УДАЛЕНИЕ РУССКИХ СИМВОЛОВ (ПЛОХОЙ ВАРИАНТ)

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
                .setShop(scrapperOffer.getShopName())
                .setAge(scrapperOffer.getAge());
    }

    private void sendOffersToAggregator(List<FinalOffer> finalOffers) {
        String url = AGGREGATOR_SERVICE_BASE_PATH + AGGREGATOR_SERVICE_RECEIVE_FINAL_OFFERS_PATH;
        restTemplate.exchange(
                url,
                HttpMethod.POST,
                new HttpEntity<>(finalOffers),
                new ParameterizedTypeReference<List<FinalOffer>>() {
                }
        );
    }
}
