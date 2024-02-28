package by.bsuir.lookmanager.entities.product.information;

import by.bsuir.lookmanager.entities.product.ProductEntity;
import by.bsuir.lookmanager.enums.MediaType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "media")
public class Media {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "product_id")
    private ProductEntity product;
    @Column(name = "type")
    private MediaType type;
    @Column (name = "media_file")
    private byte[] file;
}
