package ru.vakoom.matchingservice.scheduler;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import ru.vakoom.matchingservice.model.Product;
import ru.vakoom.matchingservice.service.AggregatorInfoService;
import ru.vakoom.matchingservice.service.ProductService;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ProductRefresher {

    private final ProductService productService;
    private final AggregatorInfoService aggregatorInfoService;

    @Scheduled(cron = "0 0 */12 * * *") // every 12 hours
    public void refreshProducts() {
        List<Product> products = aggregatorInfoService.getAll();
        productService.save(products);
    }

}
