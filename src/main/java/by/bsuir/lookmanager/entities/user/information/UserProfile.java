package by.bsuir.lookmanager.entities.user.information;

import by.bsuir.lookmanager.enums.UserGender;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcType;
import org.hibernate.dialect.PostgreSQLEnumJdbcType;

import java.sql.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table (name = "profile")
public class UserProfile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "firstname")
    private String firstname;
    @Column(name = "lastname")
    private String lastname;
    @Column(name = "date_of_birth")
    private Date dateOfBirth;
    @Column(name = "address")
    private String address;
    @Column(name = "phone_number")
    private String phoneNumber;
    @Enumerated(EnumType.STRING)
    @Column(name = "gender", columnDefinition = "user_gender")
    @JdbcType(PostgreSQLEnumJdbcType.class)
    private UserGender gender;
    @Column(name = "postal_code")
    private String postalCode;
    @ManyToOne
    @JoinColumn (name="city_id")
    private City city;
    @ManyToOne
    @JoinColumn (name="country_id")
    private Country country;
    @Column(name = "profile_image", columnDefinition = "bytea")
    private byte[] imageData;
}
