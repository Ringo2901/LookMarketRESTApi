package by.bsuir.lookmanager.dao;

import by.bsuir.lookmanager.dto.product.general.GeneralProductResponseDto;
import by.bsuir.lookmanager.entities.product.ProductEntity;
import jakarta.persistence.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;

public interface ProductRepository extends JpaRepository<ProductEntity, Long>, JpaSpecificationExecutor<ProductEntity> {
    Page<ProductEntity> findAll(Specification<ProductEntity> spec, Pageable pageable);

    //@Query(value = "SELECT * FROM get_products(:query, :pageSize, :pageNumber, :sortBy, :sortOrder, :size, :color, :brand, :filtSeason, :filtGender, :filtAgeType, :tags, :materials, :subcategory, :category, :minPrice, :maxPrice)", nativeQuery = true)
    //@Procedure(procedureName = "get_products")
    //List<GeneralProductResponseDto> getProducts(@Param("query")String query, @Param("pageSize")int pageSize, @Param("pageNumber")int pageNumber, @Param("sortBy")String sortBy, @Param("sortOrder")String sortOrder, @Param("size")Integer[] size, @Param("color")String[] color, @Param("brand")String brand, @Param("filtSeason")String[] filtSeason, @Param("filtGender")String[] filtGender, @Param("filtAgeType")String[] filtAgeType, @Param("tags")String[] tags, @Param("materials")String[] materials, @Param("subcategory")String[] subcategory, @Param("category")String[] category, @Param("minPrice")double minPrice, @Param("maxPrice")double maxPrice);
    //}
}