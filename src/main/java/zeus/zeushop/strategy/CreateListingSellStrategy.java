package zeus.zeushop.strategy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import zeus.zeushop.model.ListingSell;
import zeus.zeushop.repository.ListingSellRepository;

@Component
public class CreateListingSellStrategy implements ListingSellStrategy {

    private final ListingSellRepository listingSellRepository;

    @Autowired
    public CreateListingSellStrategy(ListingSellRepository listingSellRepository) {
        this.listingSellRepository = listingSellRepository;
    }

    @Override
    public void execute(ListingSell listingSell) {
        listingSellRepository.save(listingSell);
    }
}