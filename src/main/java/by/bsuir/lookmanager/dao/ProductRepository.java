package by.bsuir.lookmanager.dao;

import by.bsuir.lookmanager.entities.product.ProductEntity;
import org.springframework.data.repository.CrudRepository;

public interface ProductRepository extends CrudRepository<ProductEntity, Long> {
}
