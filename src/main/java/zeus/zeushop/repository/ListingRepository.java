package zeus.zeushop.repository;
import org.springframework.data.jpa.repository.Query;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import zeus.zeushop.model.Listing;
import java.time.LocalDateTime;

import java.util.List;

@Repository
public interface ListingRepository extends JpaRepository<Listing, Integer> {
    @Query("SELECT l FROM Listing l WHERE l.end_date > :currentTime")
    List<Listing> findByEndDateGreaterThan(LocalDateTime currentTime);
    @Query("SELECT l FROM Listing l WHERE l.seller_id = :sellerId")
    List<Listing> findBySellerId(Integer sellerId);
}
