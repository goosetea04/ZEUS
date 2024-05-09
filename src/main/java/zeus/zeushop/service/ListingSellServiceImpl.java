package zeus.zeushop.service;

import org.springframework.beans.factory.annotation.Autowired;
import zeus.zeushop.model.ListingSell;
import zeus.zeushop.repository.ListingSellRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ListingSellServiceImpl implements ListingSellService {
    private final ListingSellRepository listingSellRepository;

    @Autowired
    public ListingSellServiceImpl(ListingSellRepository listingSellRepository) {
        this.listingSellRepository = listingSellRepository;
    }

    @Override
    public ListingSell create(ListingSell listingSell) {
        listingSellRepository.save(listingSell);
        return listingSell;
    }

    @Override
    public List<ListingSell> findAll() {
        return listingSellRepository.findAll();
    }

    @Override
    public Optional<ListingSell> findById(Integer Id) {
        return listingSellRepository.findById(Id);
    }

    @Override
    public ListingSell editListingSell(Integer id, ListingSell editedListingSell) {
        Optional<ListingSell> listingData = listingSellRepository.findById(id);
        if (listingData.isPresent()) {
            ListingSell list = listingData.get();
            list.setProduct_name(editedListingSell.getProduct_name());
            list.setProduct_description(editedListingSell.getProduct_description());
            list.setProduct_price(Math.max(0, editedListingSell.getProduct_price()));
            list.setProduct_quantity(Math.max(0, editedListingSell.getProduct_quantity()));
            list.setEndDate(editedListingSell.getEndDate());
            return listingSellRepository.save(list);
        } else {
            return null;
        }
    }

    @Override
    public ListingSell deleteListingSell(Integer id) {
        Optional<ListingSell> listingData = listingSellRepository.findById(id);
        if (listingData.isPresent()) {
            ListingSell listingToDelete = listingData.get();
            listingSellRepository.delete(listingToDelete);
            return listingToDelete;
        }
        return null;
    }
}