package by.bsuir.lookmanager.dao;

import by.bsuir.lookmanager.entities.product.information.Condition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ConditionRepository extends JpaRepository<Condition, Long>, JpaSpecificationExecutor<Condition> {
}

