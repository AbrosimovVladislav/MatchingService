package ru.vakoom.matchingservice.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.vakoom.matchingservice.model.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
