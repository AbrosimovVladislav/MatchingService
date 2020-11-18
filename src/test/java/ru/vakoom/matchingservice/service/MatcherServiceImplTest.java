package ru.vakoom.matchingservice.service;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.vakoom.matchingservice.client.AggregatorClient;
import ru.vakoom.matchingservice.client.TroubleTicketClient;
import ru.vakoom.matchingservice.model.FinalOffer;
import ru.vakoom.matchingservice.model.ScrapperOffer;
import ru.vakoom.matchingservice.model.Type;
import ru.vakoom.matchingservice.repo.MatcherOfferRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
public class MatcherServiceImplTest {

    @Autowired
    MatcherService matcherService;

    @MockBean
    MatcherOfferRepository matcherOfferRepository;

    @MockBean
    TroubleTicketClient troubleTicketClient;
    @MockBean
    AggregatorClient aggregatorClient;

    @Captor
    ArgumentCaptor<List<FinalOffer>> captor;


    List<ScrapperOffer> scrapperOffers = new ArrayList<>();

    @Test
    public void matchOffersTest() {
        when(matcherOfferRepository.findByNameAndShopAndBrandAndAge(any(), any(), any(), any())).thenReturn(Optional.empty());
        doNothing().when(troubleTicketClient).sendToTroubleTicket(any(), any());

        when(aggregatorClient.sendOffersToAggregator(any())).thenReturn(new ResponseEntity<>(HttpStatus.OK));

        for (int i = 1; i < 11; i++) {
            scrapperOffers.add(new ScrapperOffer()
                    .setId(String.valueOf(i))
                    .setAge("SR")
                    .setBrand("BAUER")
                    .setInStore(true)
                    .setLink("Link " + i)
                    .setName("Name " + i)
                    .setPrice(i * 100D)
                    .setShopName("ShopName " + i)
                    .setType(new Type()));
        }

        matcherService.matchOffers(scrapperOffers);
        verify(aggregatorClient).sendOffersToAggregator(captor.capture());


        System.out.println(captor.getValue());
    }

}
