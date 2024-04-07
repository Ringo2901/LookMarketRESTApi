package by.bsuir.lookmanager.dao;

import by.bsuir.lookmanager.dto.product.general.GeneralProductResponseDto;
import by.bsuir.lookmanager.enums.ProductStatus;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

@Repository
public class ProductNativeRepository {
    @PersistenceContext
    private EntityManager entityManager;
    @Autowired
    private DataSource dataSource;

    public List<GeneralProductResponseDto> getProducts(String query, Integer pageSize, Integer pageNumber, String sortBy, String sortOrder, Integer[] size, String[] color, String brand, String[] filtSeason, String[] filtGender, String[] filtAgeType, String[] tags, String[] materials, String[] subcategory, String[] category, Double minPrice, Double maxPrice) throws SQLException {
        List<GeneralProductResponseDto> result = new ArrayList<>();

        Session session = entityManager.unwrap(Session.class);
        session.doWork(connection -> {
            try (CallableStatement function = connection.prepareCall(
                    "{ ? = call get_products(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) }")) {
                function.registerOutParameter(1, Types.OTHER);
                function.setString(2, query);
                function.setInt(3, pageSize);
                function.setInt(4, pageNumber);
                function.setString(5, sortBy);
                function.setString(6, sortOrder);
                function.setArray(7, connection.createArrayOf("integer", size));
                function.setArray(8, connection.createArrayOf("text", color));
                function.setString(9, brand);
                function.setArray(10, connection.createArrayOf("text", filtSeason));
                function.setArray(11, connection.createArrayOf("text", filtGender));
                function.setArray(12, connection.createArrayOf("text", filtAgeType));
                function.setArray(13, connection.createArrayOf("text", tags));
                function.setArray(14, connection.createArrayOf("text", materials));
                function.setArray(15, connection.createArrayOf("text", subcategory));
                function.setArray(16, connection.createArrayOf("text", category));
                function.setDouble(17, minPrice);
                function.setDouble(18, maxPrice);
                function.execute();

                ResultSet resultSet = (ResultSet) function.getObject(1);

                while (resultSet.next()) {
                    GeneralProductResponseDto product = new GeneralProductResponseDto();
                    product.setId(resultSet.getLong("id"));
                    product.setTitle(resultSet.getString("title"));
                    product.setStatus(ProductStatus.valueOf(resultSet.getString("status")));
                    product.setCreatedTime(resultSet.getTimestamp("created_time"));
                    product.setUpdateTime(resultSet.getTimestamp("update_time"));
                    product.setPrice(resultSet.getDouble("price"));
                    product.setSubCategoryName(resultSet.getString("sub_category_name"));
                    product.setCategoryName(resultSet.getString("category_name"));
                    product.setLogin(resultSet.getString("user_login"));
                    product.setUserId(resultSet.getLong("user_id"));
                    product.setImageId(resultSet.getLong("media_id"));
                    product.setImageData(resultSet.getString("media_file"));
                    result.add(product);
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });

        return result;
    }
}
