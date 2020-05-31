package ru.vakoom.matchingservice.client;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ru.vakoom.matchingservice.model.Product;
import ru.vakoom.matchingservice.model.ScrapperOffer;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TroubleTicketClient {

    public static final String TT_BASE_PATH = "http://localhost:8083";
    public static final String TT_SEND_TICKET_PATH = "/tickets";

    private final RestTemplate restTemplate = new RestTemplate();

    public void sendToTroubleTicket(ScrapperOffer scrapperOffer, List<Product> products) {
        System.out.println("Sended to TT:" + scrapperOffer);
    }

/*    public void sendToTroubleTicket(ScrapperOffer scrapperOffer, List<Product> products) {
        String url = TT_BASE_PATH + TT_SEND_TICKET_PATH;
        restTemplate.exchange(
                url,
                HttpMethod.POST,
                new HttpEntity<>(new Ticket(scrapperOffer, products, LocalDateTime.now())),
                new ParameterizedTypeReference<>() {
                }
        );

    }*/

}
