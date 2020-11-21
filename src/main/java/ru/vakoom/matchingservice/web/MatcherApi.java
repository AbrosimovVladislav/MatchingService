package ru.vakoom.matchingservice.web;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import ru.vakoom.matchingservice.model.FinalOffer;
import ru.vakoom.matchingservice.model.MatcherOffer;
import ru.vakoom.matchingservice.model.ScrapperOffer;

import java.util.List;

public interface MatcherApi {

    @ApiOperation(value = "Receive offers",
            notes = "Receive scrapper offers from Scrapping service -> match scrapper offers with existing products -> create and send final offers to Aggregator service",
            response = ResponseEntity.class)
    ResponseEntity<List<FinalOffer>> receiveOffers(
            @ApiParam(value = "List of scrapper offers for matching procedure")
            @RequestBody List<ScrapperOffer> body);

    @ApiOperation(value = "Create new matcher offer",
            notes = "Save input matcher offer to db",
            response = ResponseEntity.class)
    MatcherOffer createMatcherOffer(
            @ApiParam(value = "Input matcher offer")
            @RequestBody MatcherOffer matcherOffer);

}
