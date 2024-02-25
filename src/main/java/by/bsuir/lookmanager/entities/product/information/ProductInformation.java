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
    @Enumerated(EnumType.STRING)
    @Column(name = "gender")
    private ProductGender gender;
    @Column(name = "description")
    private String description;
    @Enumerated(EnumType.STRING)
    @Column(name = "season")
    private Season season;
    @Enumerated(EnumType.STRING)
    @Column(name = "condition")
    private Condition condition;
    @Enumerated(EnumType.STRING)
    @Column(name = "age_type")
    private AgeType ageType;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "brand_id")
    private ProductBrand productBrand;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "sub_category_id")
    private SubCategory subCategory;
    @ManyToMany (fetch = FetchType.EAGER)
    @JoinTable(name = "product_size",
    joinColumns = @JoinColumn(name = "product_id"),
    inverseJoinColumns = @JoinColumn(name = "size_id"))
    private List<ProductSize> sizes;
    @ManyToMany (fetch = FetchType.EAGER)
    @JoinTable(name = "product_color",
            joinColumns = @JoinColumn(name = "product_id"),
            inverseJoinColumns = @JoinColumn(name = "color_id"))
    private List<ProductColor> colors;
    @ManyToMany (fetch = FetchType.EAGER)
    @JoinTable(name = "product_material",
            joinColumns = @JoinColumn(name = "product_id"),
            inverseJoinColumns = @JoinColumn(name = "material_id"))
    private List<ProductMaterial> materials;
    @ManyToMany (fetch = FetchType.EAGER)
    @JoinTable(name = "product_tag",
            joinColumns = @JoinColumn(name = "product_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id"))
    private List<ProductTag> tags;
}
