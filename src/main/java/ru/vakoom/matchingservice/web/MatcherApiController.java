package ru.vakoom.matchingservice.web;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.vakoom.matchingservice.model.FinalOffer;
import ru.vakoom.matchingservice.model.MatcherOffer;
import ru.vakoom.matchingservice.model.ScrapperOffer;
import ru.vakoom.matchingservice.scheduler.ProductRefresher;
import ru.vakoom.matchingservice.service.MatcherService;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class MatcherApiController {

    private final MatcherService matcherService;

    @PostMapping("/receiveOffers")
    public ResponseEntity<List<FinalOffer>> receiveOffers(@RequestBody List<ScrapperOffer> body) {
        return matcherService.matchOffers(body);
    }

    @Autowired
    ProductRefresher productRefresher;

    @GetMapping("/refreshProducts")
    public void test() {
        productRefresher.refreshProducts();
    }

    @PostMapping("/matcher")
    public MatcherOffer createMatcherOffer(@RequestBody MatcherOffer matcherOffer) {
        return matcherService.save(matcherOffer);
    }

}
