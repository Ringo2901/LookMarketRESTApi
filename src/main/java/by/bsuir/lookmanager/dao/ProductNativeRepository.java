package by.bsuir.lookmanager.dao;

import by.bsuir.lookmanager.dto.product.general.GeneralProductResponseDto;
import jakarta.persistence.EntityManager;
import jakarta.persistence.ParameterMode;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.StoredProcedureQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

@Repository
public class ProductNativeRepository {
    @PersistenceContext
    private EntityManager entityManager;
    @Autowired
    private DataSource dataSource;

    public List<GeneralProductResponseDto> getProducts(String query, Integer pageSize, Integer pageNumber, String sortBy, String sortOrder, Integer[] size, String[] color, String brand, String[] filtSeason, String[] filtGender, String[] filtAgeType, String[] tags, String[] materials, String[] subcategory, String[] category, Double minPrice, Double maxPrice) throws SQLException {
        Connection connection = dataSource.getConnection();
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
                .setParameter("size", connection.createArrayOf("integer", size))
                .setParameter("color", connection.createArrayOf("text", color))
                .setParameter("brand", brand)
                .setParameter("filtSeason", connection.createArrayOf("text", filtSeason))
                .setParameter("filtGender", connection.createArrayOf("text", filtGender))
                .setParameter("filtAgeType", connection.createArrayOf("text", filtAgeType))
                .setParameter("tags", connection.createArrayOf("text", tags))
                .setParameter("materials", connection.createArrayOf("text", materials))
                .setParameter("subcategory", connection.createArrayOf("text", subcategory))
                .setParameter("category", connection.createArrayOf("text", category))
                .setParameter("minPrice", minPrice)
                .setParameter("maxPrice", maxPrice);

        return storedProcedureQuery.getResultList();
}
}
