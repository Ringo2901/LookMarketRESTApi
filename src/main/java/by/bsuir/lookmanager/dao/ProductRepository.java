package by.bsuir.lookmanager.dao;

import by.bsuir.lookmanager.dto.product.general.GeneralProductResponseDto;
import by.bsuir.lookmanager.entities.product.ProductEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;

import java.util.List;

public interface ProductRepository extends JpaRepository<ProductEntity, Long>, JpaSpecificationExecutor<ProductEntity> {
    Page<ProductEntity> findAll(Specification<ProductEntity> spec, Pageable pageable);

    @Query(value = "SELECT * FROM get_products(:query, :pageSize, :pageNumber, :sortBy, :sortOrder, :size, :color, :brand, :filtSeason, :filtGender, :filtAgeType, :tags, :materials, :subcategory, :category, :minPrice, :maxPrice)", nativeQuery = true)
    List<GeneralProductResponseDto> getProducts(String query, int pageSize, int pageNumber, String sortBy, String sortOrder, Integer[] size, String[] color, String brand, String[] filtSeason, String[] filtGender, String[] filtAgeType, String[] tags, String[] materials, String[] subcategory, String[] category, double minPrice, double maxPrice);

}