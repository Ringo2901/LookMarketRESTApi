package by.bsuir.lookmanager.entities.product.information;

import by.bsuir.lookmanager.entities.product.ProductEntity;
import by.bsuir.lookmanager.enums.AgeType;
import by.bsuir.lookmanager.enums.Condition;
import by.bsuir.lookmanager.enums.ProductGender;
import by.bsuir.lookmanager.enums.Season;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcType;
import org.hibernate.annotations.Type;
import org.hibernate.dialect.PostgreSQLEnumJdbcType;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "product_information")
public class ProductInformation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "price")
    private Double price;
    @Enumerated(EnumType.STRING)
    @Column(name = "gender", columnDefinition = "product_type")
    @JdbcType(PostgreSQLEnumJdbcType.class)
    private ProductGender gender;
    @Column(name = "description")
    private String description;
    @Column(name = "latitude")
    private Double latitude;
    @Column(name = "longitude")
    private Double longitude;
    @Enumerated(EnumType.STRING)
    @JdbcType(PostgreSQLEnumJdbcType.class)
    @Column(name = "season", columnDefinition = "season")
    private Season season;
    @Enumerated(EnumType.STRING)
    @JdbcType(PostgreSQLEnumJdbcType.class)
    @Column(name = "condition", columnDefinition = "product_condition")
    private Condition condition;
    @Enumerated(EnumType.STRING)
    @JdbcType(PostgreSQLEnumJdbcType.class)
    @Column(name = "age_type", columnDefinition = "age_type")
    private AgeType ageType;
    @ManyToOne(cascade = CascadeType.DETACH)
    @JoinColumn(name = "brand_id")
    private ProductBrand productBrand;
    @ManyToMany (fetch = FetchType.EAGER, cascade = CascadeType.DETACH)
    @JoinTable(name = "product_size",
    joinColumns = @JoinColumn(name = "product_id"),
    inverseJoinColumns = @JoinColumn(name = "size_id"))
    private List<ProductSize> sizes;
    @ManyToMany (fetch = FetchType.EAGER, cascade = CascadeType.DETACH)
    @JoinTable(name = "product_color",
            joinColumns = @JoinColumn(name = "product_id"),
            inverseJoinColumns = @JoinColumn(name = "color_id"))
    private List<ProductColor> colors;
    @ManyToMany (fetch = FetchType.EAGER, cascade = CascadeType.DETACH)
    @JoinTable(name = "product_material",
            joinColumns = @JoinColumn(name = "product_id"),
            inverseJoinColumns = @JoinColumn(name = "material_id"))
    private List<ProductMaterial> materials;
    @ManyToMany (fetch = FetchType.EAGER, cascade = CascadeType.DETACH)
    @JoinTable(name = "product_tag",
            joinColumns = @JoinColumn(name = "product_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id"))
    private List<ProductTag> tags;
}
