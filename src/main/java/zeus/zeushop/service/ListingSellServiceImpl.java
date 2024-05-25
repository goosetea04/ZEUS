package zeus.zeushop.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import zeus.zeushop.model.ListingSell;
import zeus.zeushop.repository.ListingSellRepository;
import zeus.zeushop.strategy.ListingSellStrategy;

import java.util.List;
import java.util.Optional;

@Service
public class ListingSellServiceImpl implements ListingSellService {

    private final ListingSellRepository listingSellRepository;
    private final ListingSellStrategy createStrategy;
    private final ListingSellStrategy editStrategy;
    private final ListingSellStrategy deleteStrategy;

    @Autowired
    public ListingSellServiceImpl(
            ListingSellRepository listingSellRepository,
            @Qualifier("createListingSellStrategy") ListingSellStrategy createStrategy,
            @Qualifier("editListingSellStrategy") ListingSellStrategy editStrategy,
            @Qualifier("deleteListingSellStrategy") ListingSellStrategy deleteStrategy) {
        this.listingSellRepository = listingSellRepository;
        this.createStrategy = createStrategy;
        this.editStrategy = editStrategy;
        this.deleteStrategy = deleteStrategy;
    }

    @Override
    public ListingSell create(ListingSell listingSell) {
        createStrategy.execute(listingSell);
        return listingSell;
    }

    @Override
    public List<ListingSell> findAll() {
        return listingSellRepository.findAll();
    }

    @Override
    public Optional<ListingSell> findById(Integer id) {
        return listingSellRepository.findById(id);
    }

    @Override
    public ListingSell editListingSell(Integer id, ListingSell editedListingSell) {
        editedListingSell.setProduct_id(id);
        editStrategy.execute(editedListingSell);
        return editedListingSell;
    }

    @Override
    public ListingSell deleteListingSell(Integer id) {
        ListingSell listingSell = new ListingSell();
        listingSell.setProduct_id(id);
        deleteStrategy.execute(listingSell);
        return listingSell;
    }

    @Override
    public List<ListingSell> findBySellerId(Integer sellerId) {
        return listingSellRepository.findBySellerId(sellerId);
    }
}

