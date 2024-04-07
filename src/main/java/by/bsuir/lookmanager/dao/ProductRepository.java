package by.bsuir.lookmanager.dao;

import by.bsuir.lookmanager.dto.product.general.GeneralProductResponseDto;
import by.bsuir.lookmanager.entities.product.ProductEntity;
import by.bsuir.lookmanager.enums.AgeType;
import by.bsuir.lookmanager.enums.ProductGender;
import by.bsuir.lookmanager.enums.Season;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductRepository extends JpaRepository<ProductEntity, Long>, JpaSpecificationExecutor<ProductEntity> {
    Page<ProductEntity> findAll(Specification<ProductEntity> spec, Pageable pageable);

    @Procedure
    List<GeneralProductResponseDto> getProducts(String query, Integer pageSize, Integer pageNumber, String sortBy, String sortOrder,
                                                List<Integer> size, List<String> color, String brand, List<String> filtSeason, List<String> filtGender,
                                                List<String> filtAgeType, List<String> tags, List<String> materials, List<String> subcategory, List<String> category,
                                                Double minPrice, Double maxPrice);
}