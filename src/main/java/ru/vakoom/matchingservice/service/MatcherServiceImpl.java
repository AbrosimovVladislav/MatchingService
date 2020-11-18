package ru.vakoom.matchingservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.vakoom.matchingservice.annotation.Profiling;
import ru.vakoom.matchingservice.client.AggregatorClient;
import ru.vakoom.matchingservice.client.TroubleTicketClient;
import ru.vakoom.matchingservice.model.FinalOffer;
import ru.vakoom.matchingservice.model.MatcherOffer;
import ru.vakoom.matchingservice.model.Product;
import ru.vakoom.matchingservice.model.ScrapperOffer;
import ru.vakoom.matchingservice.repo.MatcherOfferRepository;
import ru.vakoom.matchingservice.repo.ProductRepository;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@Profiling
@RequiredArgsConstructor
public class MatcherServiceImpl implements MatcherService {

    private final MatcherOfferRepository matcherOfferRepository;
    private final ProductRepository productRepository;
    private final TroubleTicketClient troubleTicketClient;
    private final AggregatorClient aggregatorClient;

    private List<Product> products;

    @Override
    public MatcherOffer save(MatcherOffer matcherOffer) {
        return matcherOfferRepository.save(matcherOffer);
    }

    @Override
    public ResponseEntity<List<FinalOffer>> matchOffers(List<ScrapperOffer> scrapperOffers) {
        log.info("Input scrapper offers from scrapper service size: {}", scrapperOffers.size());
        products = productRepository.findAll();
        List<FinalOffer> finalOffers = new ArrayList<>();
        for (var scrapperOffer : scrapperOffers) {
            getMatcherOfferByScrapperOffer(scrapperOffer)
                    .or(() -> matchOfferWithProducts(scrapperOffer).map(matcherOfferRepository::save))
                    .ifPresent(matcherOffer -> finalOffers.add(convertToFinalOffer(scrapperOffer, matcherOffer)));
        }
        log.info("Output final offers from matching service size: {}", finalOffers.size());
        return aggregatorClient.sendOffersToAggregator(finalOffers);
    }

    private Optional<MatcherOffer> getMatcherOfferByScrapperOffer(ScrapperOffer scrapperOffer) {
        return matcherOfferRepository.findByNameAndShopAndBrandAndAge(scrapperOffer.getName(),
                scrapperOffer.getShopName(),
                scrapperOffer.getBrand(),
                scrapperOffer.getAge());
    }

    private Optional<MatcherOffer> matchOfferWithProducts(ScrapperOffer scrapperOffer) {
        List<Product> matchedProducts = products.stream()
                .filter(product -> product.getType().equals(scrapperOffer.getType()))
                .filter(product -> product.getBrand().equalsIgnoreCase(scrapperOffer.getBrand()) || scrapperOffer.getBrand().isBlank())
                .filter(product -> product.getAge().equalsIgnoreCase(scrapperOffer.getAge()) || scrapperOffer.getAge().isBlank())
                .filter(product -> match(product, scrapperOffer))
                .collect(Collectors.toList());

        if (matchedProducts.size() == 1) {
            return Optional.of(createMatcherOffer(scrapperOffer, matchedProducts.get(0)));
        } else {
            troubleTicketClient.sendToTroubleTicket(scrapperOffer, matchedProducts);
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
       /* if (productNameArr.size() != offerNameArr.size()) return false;
        Collections.sort(productNameArr);
        Collections.sort(offerNameArr);
        return Objects.equals(productNameArr, offerNameArr);*/
        //ToDo два варинта со склейкой в строку и ос равнением в массив
        //ToDo если к сравнивать два массива теряются кейсы в стиле (X 2.7 и X2.7 разные товары)
        //ToDo если склеивать строки, то можем получить ложный матч
        Collections.sort(productNameArr);
        Collections.sort(offerNameArr);
        String productForChecking = String.join("", productNameArr).replace(" ", "");
        String offerForChecking = String.join("", offerNameArr).replace(" ", "");
        return Objects.equals(productForChecking, offerForChecking);
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

    private MatcherOffer createMatcherOffer(ScrapperOffer scrapperOffer, Product product) {
        return new MatcherOffer()
                .setBrand(scrapperOffer.getBrand())
                .setName(scrapperOffer.getName())
                .setProductId(product.getProductId())
                .setShop(scrapperOffer.getShopName())
                .setAge(scrapperOffer.getAge())
                .setType(scrapperOffer.getType());
    }

}
