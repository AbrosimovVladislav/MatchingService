package ru.vakoom.matchingservice.client;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ru.vakoom.matchingservice.model.MatcherOffer;

@Slf4j
@Service
@RequiredArgsConstructor
public class TrustInfoClient {

    public static final String TRUST_INFO_BASE_PATH = "http://localhost:8086";
    public static final String MATCHER_CREATE_TRUST_INFO_PATH = "/matcherOffer";

    private final RestTemplate restTemplate = new RestTemplate();

    public MatcherOffer saveToTrustInfo(MatcherOffer matcherOffer) {
        String url = TRUST_INFO_BASE_PATH + MATCHER_CREATE_TRUST_INFO_PATH;
        return restTemplate.exchange(
                url,
                HttpMethod.POST,
                new HttpEntity<>(matcherOffer),
                new ParameterizedTypeReference<MatcherOffer>() {
                }
        ).getBody();
    }
}
