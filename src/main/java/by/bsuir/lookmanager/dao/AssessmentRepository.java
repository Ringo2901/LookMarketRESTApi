package by.bsuir.lookmanager.dao;

import by.bsuir.lookmanager.entities.user.information.Assessments;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Optional;

public interface AssessmentRepository extends JpaRepository<Assessments, Long>, JpaSpecificationExecutor<Assessments> {
    Optional<Assessments> findFirstByUserIdAndSellerId (Long userId, Long sellerId);
    List<Assessments> findBySellerId (Long sellerId);
    boolean existsByUserIdAndSellerId (Long userId, Long sellerId);
}
