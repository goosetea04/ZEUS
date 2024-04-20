package zeus.zeushop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import zeus.zeushop.model.Listing;

import java.time.LocalDateTime;
import java.util.List;

public interface ListingRepository extends JpaRepository<Listing, Long> {
    List<Listing> findByEndDateGreaterThanEqual(LocalDateTime currentDateTime);
}