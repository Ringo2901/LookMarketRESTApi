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

    public List<GeneralProductResponseDto> getProducts(Long userId, String query, Integer pageSize, Integer pageNumber, String sortBy, String sortOrder, Integer[] size, String[] color, String[] brand, String[] filtSeason, String[] filtGender, String[] filtAgeType, String[] tags, String[] materials, String[] subcategory, String[] category, Double minPrice, Double maxPrice) throws SQLException {
        String sql = "SELECT * FROM get_products(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        if (isEmptyArray(size)) size = null;
        if (isEmptyArray(color)) color = null;
        if (isEmptyArray(brand)) brand = null;
        if (isEmptyArray(filtSeason)) filtSeason = null;
        if (isEmptyArray(filtGender)) filtGender = null;
        if (isEmptyArray(filtAgeType)) filtAgeType = null;
        if (isEmptyArray(tags)) tags = null;
        if (isEmptyArray(materials)) materials = null;
        if (isEmptyArray(subcategory)) subcategory = null;
        if (isEmptyArray(category)) category = null;

        return jdbcTemplate.query(sql, new Object[]{userId, query, pageSize, pageNumber, sortBy, sortOrder, size, color, brand, filtSeason, filtGender, filtAgeType, tags, materials, subcategory, category, minPrice, maxPrice}, new BeanPropertyRowMapper<>(GeneralProductResponseDto.class));
    }
    public Long getProductCount(Long userId, String query, String sortBy, String sortOrder, Integer[] size, String[] color, String[] brand, String[] filtSeason, String[] filtGender, String[] filtAgeType, String[] tags, String[] materials, String[] subcategory, String[] category, Double minPrice, Double maxPrice) throws SQLException {
        String sql = "SELECT get_products_count(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        if (isEmptyArray(size)) size = null;
        if (isEmptyArray(color)) color = null;
        if (isEmptyArray(brand)) brand = null;
        if (isEmptyArray(filtSeason)) filtSeason = null;
        if (isEmptyArray(filtGender)) filtGender = null;
        if (isEmptyArray(filtAgeType)) filtAgeType = null;
        if (isEmptyArray(tags)) tags = null;
        if (isEmptyArray(materials)) materials = null;
        if (isEmptyArray(subcategory)) subcategory = null;
        if (isEmptyArray(category)) category = null;

        return jdbcTemplate.queryForObject(sql, new Object[]{userId, query, sortBy, sortOrder, size, color, brand, filtSeason, filtGender, filtAgeType, tags, materials, subcategory, category, minPrice, maxPrice}, Long.class);
    }
    private boolean isEmptyArray(Object[] array) {
        return array == null || array.length == 0;
    }
}
