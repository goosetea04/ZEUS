package zeus.zeushop.strategy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import zeus.zeushop.model.ListingSell;
import zeus.zeushop.repository.ListingSellRepository;

import java.util.Optional;

@Component
public class EditListingSellStrategy implements ListingSellStrategy {

    private final ListingSellRepository listingSellRepository;

    @Autowired
    public EditListingSellStrategy(ListingSellRepository listingSellRepository) {
        this.listingSellRepository = listingSellRepository;
    }

    @Override
    public void execute(ListingSell listingSell) {
        Optional<ListingSell> listingData = listingSellRepository.findById(listingSell.getProduct_id());
        if (listingData.isPresent()) {
            ListingSell list = listingData.get();
            list.setProduct_name(listingSell.getProduct_name());
            list.setProduct_description(listingSell.getProduct_description());
            list.setProduct_price(Math.max(0, listingSell.getProduct_price()));
            list.setProduct_quantity(Math.max(0, listingSell.getProduct_quantity()));
            list.setEndDate(listingSell.getEndDate());
            listingSellRepository.save(list);
        }
    }
}
