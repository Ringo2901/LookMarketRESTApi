package by.bsuir.lookmanager.dao;

import by.bsuir.lookmanager.dto.product.general.GeneralProductResponseDto;
import jakarta.persistence.EntityManager;
import jakarta.persistence.ParameterMode;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.StoredProcedureQuery;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ProductNativeRepository {
    @PersistenceContext
    private EntityManager entityManager;

    public List<GeneralProductResponseDto> getProducts(String query, Integer pageSize, Integer pageNumber, String sortBy, String sortOrder, Integer[] size, String[] color, String brand, String[] filtSeason, String[] filtGender, String[] filtAgeType, String[] tags, String[] materials, String[] subcategory, String[] category, Double minPrice, Double maxPrice) {
        StoredProcedureQuery storedProcedureQuery = entityManager.createStoredProcedureQuery("get_products", GeneralProductResponseDto.class)
                .registerStoredProcedureParameter("query", String.class, ParameterMode.IN)
                .registerStoredProcedureParameter("page_size", Integer.class, ParameterMode.IN)
                .registerStoredProcedureParameter("page_number", Integer.class, ParameterMode.IN)
                .registerStoredProcedureParameter("sortBy", String.class, ParameterMode.IN)
                .registerStoredProcedureParameter("sortOrder", String.class, ParameterMode.IN)
                .registerStoredProcedureParameter("size", Integer[].class, ParameterMode.IN)
                .registerStoredProcedureParameter("color", String[].class, ParameterMode.IN)
                .registerStoredProcedureParameter("brand", String.class, ParameterMode.IN)
                .registerStoredProcedureParameter("filtSeason", String[].class, ParameterMode.IN)
                .registerStoredProcedureParameter("filtGender", String[].class, ParameterMode.IN)
                .registerStoredProcedureParameter("filtAgeType", String[].class, ParameterMode.IN)
                .registerStoredProcedureParameter("tags", String[].class, ParameterMode.IN)
                .registerStoredProcedureParameter("materials", String[].class, ParameterMode.IN)
                .registerStoredProcedureParameter("subcategory", String[].class, ParameterMode.IN)
                .registerStoredProcedureParameter("category", String[].class, ParameterMode.IN)
                .registerStoredProcedureParameter("minPrice", Double.class, ParameterMode.IN)
                .registerStoredProcedureParameter("maxPrice", Double.class, ParameterMode.IN)

                .setParameter("query", query)
                .setParameter("page_size", pageSize)
                .setParameter("page_number", pageNumber)
                .setParameter("sortBy", sortBy)
                .setParameter("sortOrder", sortOrder)
                .setParameter("size", size)
                .setParameter("color", color)
                .setParameter("brand", brand)
                .setParameter("filtSeason", filtSeason)
                .setParameter("filtGender", filtGender)
                .setParameter("filtAgeType", filtAgeType)
                .setParameter("tags", tags)
                .setParameter("materials", materials)
                .setParameter("subcategory", subcategory)
                .setParameter("category", category)
                .setParameter("minPrice", minPrice)
                .setParameter("maxPrice", maxPrice);

        return storedProcedureQuery.getResultList();
}
}
