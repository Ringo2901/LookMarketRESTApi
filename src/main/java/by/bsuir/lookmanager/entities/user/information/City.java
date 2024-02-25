package by.bsuir.lookmanager.entities.user.information;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "city")
public class City {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column (name = "name")
    private String name;
    @ManyToOne (optional=false, cascade=CascadeType.ALL)
    @JoinColumn (name="country_id")
    private Country country;
}
