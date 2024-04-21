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
}