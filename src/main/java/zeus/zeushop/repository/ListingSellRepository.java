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

<<<<<<< HEAD
    // Custom method to find listings by a specific attribute, for example:
    // List<Listing> findByCategory(String category);
    List<ListingSell> findByEndDateGreaterThanEqual(LocalDateTime endDate); //ok thanks
    List<ListingSell> findByVisibleIsTrue();
    List<ListingSell> findBySellerId(Integer sellerId);
=======
    public ListingSell create(ListingSell listing) {
        listingData.add(listing);
        return listing;
    }

    public Iterator<ListingSell> findAll() {
        return listingData.iterator();
    }

    public ListingSell findById(String id) {
        for (ListingSell listingSell : listingData) {
            if (listingSell.getId().equals(id)) {
                return listingSell;
            }
        }
        return null;
    }

    public void deleteListingSell(ListingSell listingSell) {
        listingData.remove(listingSell);
    }

    public void editListingSell(ListingSell listingSell) {
        for (int i=0; i < listingData.size(); i++) {
            ListingSell addListing = listingData.get(i);
            if (addListing.getId().equals(listingSell.getId())) {
                listingData.set(i, listingSell);
                return;
            }
        }
    }
>>>>>>> aa32d76 (add delete and edit in ListingSellRepository)
}