package ru.vakoom.matchingservice.scheduler;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import ru.vakoom.matchingservice.model.Product;
import ru.vakoom.matchingservice.service.ProductService;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ProductRefresher {

    private final ProductService productService;

    @Scheduled(cron = "0 0 */12 * * *") // every 12 hours
    public void refreshProducts() {
        List<Product> products = productService.getProductsFromAggregator();
        productService.save(products);
    }

}
