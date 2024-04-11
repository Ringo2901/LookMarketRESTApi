package by.bsuir.lookmanager.dao;

import by.bsuir.lookmanager.entities.user.information.Catalog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Optional;

public interface CatalogRepository extends JpaRepository<Catalog, Long>, JpaSpecificationExecutor<Catalog> {
    List<Catalog> findByUserId (Long userId);
}
