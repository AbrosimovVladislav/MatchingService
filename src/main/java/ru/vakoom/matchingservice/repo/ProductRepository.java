package ru.vakoom.matchingservice.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import ru.vakoom.matchingservice.model.Product;

public interface ProductRepository extends JpaRepository<Product,Long> {
}
