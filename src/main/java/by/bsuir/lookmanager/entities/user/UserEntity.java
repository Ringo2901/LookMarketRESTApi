package by.bsuir.lookmanager.entities.user;

import by.bsuir.lookmanager.entities.product.ProductEntity;
import by.bsuir.lookmanager.entities.user.information.Assessments;
import by.bsuir.lookmanager.entities.user.information.SubscriptionEntity;
import by.bsuir.lookmanager.entities.user.information.UserProfile;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "login")
    private String login;
    @Column(name = "password")
    private String password;
    @Column(name = "email")
    private String email;
    @Column(name = "authorisation_status")
    private boolean authorisationStatus;
    @Column(name = "registration_date", insertable = false)
    private Date registrationDate;
    @Column(name = "last_sign_in")
    private Timestamp lastSignIn;
    @OneToOne
    @JoinColumn(name = "profile_id")
    private UserProfile userProfile;
    @ManyToMany
    @JoinTable (name="favourites",
            joinColumns=@JoinColumn (name="user_id"),
            inverseJoinColumns=@JoinColumn(name="product_id"))
    private List<ProductEntity> favouriteProducts;
}
