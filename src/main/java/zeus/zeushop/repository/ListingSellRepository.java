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
        for (ListingSell list : listingData) {
            if (list.getId().equals(id)) {
                return list;
            }
        }
        return null;
    }

    public ListingSell deleteListingSell(String id) {
        for (ListingSell list : listingData) {
            if (list.getId().equals(id)) {
                listingData.remove(list);
                return list;
            }
        }
        return null;
    }

    public ListingSell editListingSell(ListingSell editedListingSell) {
        for (ListingSell list : listingData) {
        if (list.getId().equals(editedListingSell.getId())) {
            list.setName(editedListingSell.getName());
            list.setDescription(editedListingSell.getDescription());
            list.setStock(Math.max(0, editedListingSell.getStock()));
            list.setPrice(Math.max(0, editedListingSell.getPrice()));
            return list;
            }
        }
        return null;
    }
}