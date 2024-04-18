package zeus.zeushop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import zeus.zeushop.model.Listing;

public interface ListingRepository extends JpaRepository<Listing, String> {}