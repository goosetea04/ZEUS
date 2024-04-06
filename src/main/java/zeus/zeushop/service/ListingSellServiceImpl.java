package zeus.zeushop.service;

import zeus.zeushop.model.ListingSell;
import zeus.zeushop.repository.ListingSellRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Service
public class ListingSellServiceImpl implements ListingSellService {

    @Autowired
    private ListingSellRepository listingSellRepository;

    @Override
    public ListingSell create(ListingSell listingSell) {
        listingSellRepository.create(listingSell);
        return listingSell;
    }

    @Override
    public List<ListingSell> findAll() {
        Iterator <ListingSell> listingSellIterator = listingSellRepository.findAll();
        List<ListingSell> allListingSell = new ArrayList<>();
        listingSellIterator.forEachRemaining(allListingSell::add);
        return allListingSell;
    }
}