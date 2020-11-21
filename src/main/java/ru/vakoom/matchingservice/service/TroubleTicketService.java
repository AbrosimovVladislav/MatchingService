package ru.vakoom.matchingservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.web.context.annotation.RequestScope;
import ru.vakoom.matchingservice.client.TroubleTicketClient;
import ru.vakoom.matchingservice.model.Product;
import ru.vakoom.matchingservice.model.ScrapperOffer;
import ru.vakoom.matchingservice.model.Ticket;
import ru.vakoom.matchingservice.service.aspect.logging.MeasurePerformance;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
@RequestScope
@RequiredArgsConstructor
public class TroubleTicketService {

    private final TroubleTicketClient troubleTicketClient;

    private List<Ticket> ticketBatch = new ArrayList<>();

    @MeasurePerformance
    public void sendTickets() {
        List<Ticket> newSavedTickets = troubleTicketClient.sendTickets(ticketBatch);
        if (!CollectionUtils.isEmpty(newSavedTickets)) log.info("This new tickets was added: {}", newSavedTickets);
    }

    void prepareTicketForBatch(ScrapperOffer scrapperOffer, List<Product> products) {
        Ticket ticket = new Ticket(scrapperOffer, products, LocalDateTime.now());
        ticketBatch.add(ticket);
    }

}
