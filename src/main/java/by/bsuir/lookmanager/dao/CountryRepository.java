package by.bsuir.lookmanager.dao;

import by.bsuir.lookmanager.entities.user.information.Catalog;
import by.bsuir.lookmanager.entities.user.information.Country;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface CountryRepository extends JpaRepository<Country, Long>, JpaSpecificationExecutor<Country> {
}
