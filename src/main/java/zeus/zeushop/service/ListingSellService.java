package zeus.zeushop.service;

import zeus.zeushop.model.ListingSell;
import java.util.List;

public interface ListingSellService {
    public ListingSell create(ListingSell listing);
    public List<ListingSell> findAll();
}