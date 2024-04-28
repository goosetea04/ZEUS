package zeus.zeushop.service;

import zeus.zeushop.model.ListingSell;
import java.util.List;

public interface ListingSellService {
    public ListingSell create(ListingSell listing);
    public List<ListingSell> findAll();
    public ListingSell findById(String id);
    public ListingSell deleteListingSell(String id);
    public ListingSell editListingSell(ListingSell listing);
}