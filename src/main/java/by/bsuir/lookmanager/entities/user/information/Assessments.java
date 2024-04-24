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
@Table(name = "user_assessment")
public class Assessments {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "assessment")
    private Double assessment;
    @Column(name = "description")
    private String description;
    @Column(name = "user_id")
    private Long userId;
    @Column(name = "seller_id")
    private Long sellerId;
}
