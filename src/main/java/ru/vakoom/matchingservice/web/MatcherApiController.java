package ru.vakoom.matchingservice.web;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.vakoom.matchingservice.model.ScrapperOffer;
import ru.vakoom.matchingservice.service.MatcherService;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class MatcherApiController {

    private final MatcherService matcherService;

    @PostMapping("/receiveOffers")
    public void receiveOffers(@RequestBody List<ScrapperOffer> body) {
        matcherService.matchOffers(body);
    }

}
