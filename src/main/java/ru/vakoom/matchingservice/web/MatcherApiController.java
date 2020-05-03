package ru.vakoom.matchingservice.web;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.vakoom.matchingservice.model.Offer;

import java.util.List;

@RestController
public class MatcherApiController {

    @PostMapping("/receiveOffers")
    public void receiveOffers(@RequestBody List<Offer> body) {
        body.forEach(System.out::println);
    }

}
