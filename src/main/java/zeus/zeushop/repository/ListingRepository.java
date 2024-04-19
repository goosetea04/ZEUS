package zeus.zeushop.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import zeus.zeushop.model.Listing;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
public interface ListingRepository extends JpaRepository<Listing, String> {
    private List<Listing> listings = new ArrayList<>();
  
    pubvlic Listing create(Listing listing) {
        listings.add(listing);
        return listing;
    }

    public Iterator<Listing> findAll(){
        return listings.iterator();
    }
}