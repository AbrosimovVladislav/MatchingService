package ru.vakoom.matchingservice.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.vakoom.matchingservice.model.ScrapperOffer;
import ru.vakoom.matchingservice.scheduler.ProductRefresher;

import java.util.List;

@RestController
public class MatcherApiController {

    @PostMapping("/receiveOffers")
    public void receiveOffers(@RequestBody List<ScrapperOffer> body) {
        body.forEach(System.out::println);
    }

    @Autowired
    ProductRefresher productRefresher;

    @GetMapping("/match")
    public void test(){
        productRefresher.refreshProducts();
    }

}
