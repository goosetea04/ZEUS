package zeus.zeushop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import zeus.zeushop.model.Listing;
import java.time.LocalDateTime;

import java.util.List;

@Repository
public interface ListingRepository extends JpaRepository<Listing, Long> {
    // No need to implement these methods manually, JpaRepository provides them

    // Custom method to find listings by a specific attribute, for example:
    // List<Listing> findByCategory(String category);
    List<Listing> findByEndDateGreaterThanEqual(LocalDateTime endDate);
}
