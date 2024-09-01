package by.bsuir.lookmanager.entities.user.information;

import by.bsuir.lookmanager.entities.user.UserEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "catalog")
public class Catalog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "name_en")
    private String nameEn;
    @Column(name = "name_ru")
    private String nameRu;
    @ManyToOne (optional=false, fetch = FetchType.LAZY)
    @JoinColumn (name="seller_id")
    private UserEntity user;
}
