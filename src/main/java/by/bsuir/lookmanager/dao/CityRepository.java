package by.bsuir.lookmanager.dao;

import by.bsuir.lookmanager.entities.user.information.Catalog;
import by.bsuir.lookmanager.entities.user.information.City;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface CityRepository extends JpaRepository<City, Long>, JpaSpecificationExecutor<City> {
    List<City> findByCountryId (Integer countryId);
}
