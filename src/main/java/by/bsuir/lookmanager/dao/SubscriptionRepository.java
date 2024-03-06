package by.bsuir.lookmanager.dao;

import by.bsuir.lookmanager.entities.user.information.SubscriptionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Optional;

public interface SubscriptionRepository extends JpaRepository<SubscriptionEntity, Long>, JpaSpecificationExecutor<SubscriptionEntity> {
    List<SubscriptionEntity> findAllBySellerId (Long sellerId);
    List<SubscriptionEntity> findAllBySubscriberId (Long subscriberId);
    boolean existsBySubscriberIdAndSellerId (Long subscriberId, Long sellerId);

    Optional<SubscriptionEntity> findBySubscriberIdAndSellerId (Long subscriberId, Long sellerId);
}
