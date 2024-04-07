package by.bsuir.lookmanager.dao;

import by.bsuir.lookmanager.dto.product.general.GeneralProductResponseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;
import java.util.List;

@Repository
public class ProductNativeRepository {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<GeneralProductResponseDto> getProducts(String query, Integer pageSize, Integer pageNumber, String sortBy, String sortOrder, Integer[] size, String[] color, String[] brand, String[] filtSeason, String[] filtGender, String[] filtAgeType, String[] tags, String[] materials, String[] subcategory, String[] category, Double minPrice, Double maxPrice) throws SQLException {
        String sql = "SELECT * FROM get_products(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        return jdbcTemplate.query(sql, new Object[]{query, pageSize, pageNumber, sortBy, sortOrder, size, color, brand, filtSeason, filtGender, filtAgeType, tags, materials, subcategory, category, minPrice, maxPrice}, new BeanPropertyRowMapper<>(GeneralProductResponseDto.class));
    }
}
