package zeus.zeushop.strategy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import zeus.zeushop.model.ListingSell;
import zeus.zeushop.repository.ListingSellRepository;

import java.util.Optional;

@Component
public class DeleteListingSellStrategy implements ListingSellStrategy {

    private final ListingSellRepository listingSellRepository;

    @Autowired
    public DeleteListingSellStrategy(ListingSellRepository listingSellRepository) {
        this.listingSellRepository = listingSellRepository;
    }

    @Override
    public void execute(ListingSell listingSell) {
        Optional<ListingSell> listingData = listingSellRepository.findById(listingSell.getProduct_id());
        listingData.ifPresent(listingSellRepository::delete);
    }
}
