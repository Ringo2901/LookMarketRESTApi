package by.bsuir.lookmanager.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    String login;

    String password;

    String firstname;

    String lastname;

    boolean sex;

    String email;

    @Column(name = "city_id")
    Long cityId;

    @Column(name = "country_id")
    Long countryId;

    @Column(name = "date_of_birth")
    Timestamp dateOfBirth;

    @Column(name = "authorisation_status")
    boolean authorisationStatus;
}
