package ru.vakoom.matchingservice.client;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ru.vakoom.matchingservice.model.Ticket;

import java.util.Collections;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class TroubleTicketClient {

    @Value("${troubleticket.base-path}")
    public String TT_BASE_PATH;
    @Value("${troubleticket.tickets-path}")
    public String TT_SEND_TICKET_PATH;

    private final RestTemplate restTemplate = new RestTemplate();

    public List<Ticket> sendTickets(List<Ticket> tickets) {
        String url = TT_BASE_PATH + TT_SEND_TICKET_PATH;
        try {

            ResponseEntity<List<Ticket>> response = restTemplate.exchange(
                    url,
                    HttpMethod.POST,
                    new HttpEntity<>(tickets),
                    new ParameterizedTypeReference<>() {
                    });

            return response.getBody();
        } catch (Exception e) {
            log.error("Request to TT rejected: {}", e.getMessage());
            return Collections.emptyList();
        }
    }

//    public Optional<MatcherOffer> sendToTroubleTicket(ScrapperOffer scrapperOffer, List<Product> products) {
//        String url = TT_BASE_PATH + TT_SEND_TICKET_PATH;
//        try {
//            restTemplate.exchange(
//                    url,
//                    HttpMethod.POST,
//                    new HttpEntity<>(new Ticket(scrapperOffer, products, LocalDateTime.now())),
//                    new ParameterizedTypeReference<>() {
//                    }
//            );
//            return Optional.empty();
//        } catch (Exception e) {
//            log.error("Request to TT rejected: {}", e.getMessage());
//            return Optional.empty();
//        }
//    }
}