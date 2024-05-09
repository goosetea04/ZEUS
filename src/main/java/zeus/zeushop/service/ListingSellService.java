package zeus.zeushop.service;

import org.springframework.stereotype.Service;
import zeus.zeushop.model.ListingSell;
import java.util.List;
import java.util.Optional;

@Service
public interface ListingSellService {
    public ListingSell create(ListingSell listing);
    public List<ListingSell> findAll();
    public Optional<ListingSell> findById(Integer id);
    public ListingSell editListingSell(Integer id, ListingSell editedListingSell);
    public ListingSell deleteListingSell(Integer id);
}