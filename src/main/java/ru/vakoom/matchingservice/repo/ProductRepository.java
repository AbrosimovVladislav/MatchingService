package ru.vakoom.matchingservice.repo;

import org.springframework.data.repository.CrudRepository;
import ru.vakoom.matchingservice.model.Product;

public interface ProductRepository extends CrudRepository<Product,Long> {
}
