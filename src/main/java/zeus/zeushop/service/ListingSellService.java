package zeus.zeushop.service;

import org.springframework.stereotype.Service;
import zeus.zeushop.model.ListingSell;
import java.util.List;
import java.util.Optional;
import zeus.zeushop.service.*;

@Service
public interface ListingSellService {
    ListingSell create(ListingSell listing);
    List<ListingSell> findAll();
    Optional<ListingSell> findById(Integer id);
    ListingSell editListingSell(Integer id, ListingSell editedListingSell);
    ListingSell deleteListingSell(Integer id);
    List<ListingSell> findBySellerId(Integer sellerId);
}