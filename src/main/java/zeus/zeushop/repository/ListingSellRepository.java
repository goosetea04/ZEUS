package zeus.zeushop.repository;
import zeus.zeushop.model.ListingSell;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Repository
public class ListingSellRepository {
    private List<ListingSell> listingData = new ArrayList<>();

    public ListingSell create(ListingSell listing) {
        listingData.add(listing);
        return listing;
    }

    public Iterator<ListingSell> findAll() {
        return listingData.iterator();
    }
}