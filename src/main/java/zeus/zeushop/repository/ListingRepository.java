package zeus.zeushop.repository;
import zeus.zeushop.model.Listing;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
@Repository
public class ListingRepository {
    private List<Listing> listings = new ArrayList<>();

    public Listing create(Listing listing) {
        listings.add(listing);
        return listing;
    }

    public Iterator<Listing> findAll(){
        return listings.iterator();
    }
}
