package zeus.zeushop.repository;
import org.springframework.data.jpa.repository.Query;
import zeus.zeushop.model.ListingSell;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.time.LocalDateTime;

@Repository
public interface ListingSellRepository extends JpaRepository<ListingSell, Integer> {
    // No need to implement these methods manually, JpaRepository provides them

    // Custom method to find listings by a specific attribute, for example:
    // List<Listing> findByCategory(String category);
    List<ListingSell> findByEndDateGreaterThanEqual(LocalDateTime endDate); //ok thanks
    List<ListingSell> findByVisibleIsTrue();
    List<ListingSell> findBySellerId(Integer sellerId);
}