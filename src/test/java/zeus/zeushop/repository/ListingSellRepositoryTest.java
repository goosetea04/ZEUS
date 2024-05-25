package zeus.zeushop.repository;

import org.junit.jupiter.api.BeforeEach;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import zeus.zeushop.model.ListingSell;

import java.util.Iterator;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ListingSellRepositoryTest {
    @InjectMocks
    ListingSellRepository listingSellRepository;

    @BeforeEach
    void setUp() {
    }

    @Test
    void testCreateAndFind() {
        ListingSell listingSell = new ListingSell();
        listingSell.setProduct_id(1);
        listingSell.setProduct_name("Mini Skirt");
        listingSell.setProduct_description("Lorem ipsum dolor sit amet,");
        listingSell.setProduct_quantity(10);
        listingSell.setProduct_price(129000f);
        listingSellRepository.save(listingSell);

        Iterable<ListingSell> listingSells = listingSellRepository.findAll();
        Iterator<ListingSell> listingSellIterator = listingSells.iterator();
        assertTrue(listingSellIterator.hasNext());
        ListingSell savedListingSell = listingSellIterator.next();
        assertEquals(listingSell.getProduct_id(), savedListingSell.getProduct_id());
        assertEquals(listingSell.getProduct_name(), savedListingSell.getProduct_name());
        assertEquals(listingSell.getProduct_description(), savedListingSell.getProduct_description());
        assertEquals(listingSell.getProduct_quantity(), savedListingSell.getProduct_quantity());
        assertEquals(listingSell.getProduct_price(), savedListingSell.getProduct_price());
    }

    // Other test methods can follow similarly

    @Test
    void testUpdateStockNotNegative() {
//        String id = UUID.randomUUID().toString();

        ListingSell list = new ListingSell();
        list.setProduct_id(1);
        list.setProduct_name("Mini Skirt");
        list.setProduct_description("Lorem ipsum dolor sit amet,");
        list.setProduct_quantity(10);
        list.setProduct_price(129000f);
        listingSellRepository.save(list);

        ListingSell updatedList = new ListingSell();
        updatedList.setProduct_id(1);
        updatedList.setProduct_name("Black Shirt");
        updatedList.setProduct_description("Lorem ipsum dolor sit amet,");
        updatedList.setProduct_quantity(-1);
        updatedList.setProduct_price(119000f);
        listingSellRepository.save(updatedList);

        Iterable<ListingSell> listingSells = listingSellRepository.findAll();
        ListingSell retrievedList = listingSells.iterator().next();
        assertEquals(0, retrievedList.getProduct_quantity());
    }

    @Test
    void testUpdatePriceNotNegative() {
//        String id = UUID.randomUUID().toString();

        ListingSell list = new ListingSell();
        list.setProduct_id(1);
        list.setProduct_name("Mini Skirt");
        list.setProduct_description("Lorem ipsum dolor sit amet,");
        list.setProduct_quantity(10);
        list.setProduct_price(129000f);
        listingSellRepository.save(list);

        ListingSell updatedList = new ListingSell();
        updatedList.setProduct_id(1);
        updatedList.setProduct_name("Black Shirt");
        updatedList.setProduct_description("Lorem ipsum dolor sit amet,");
        updatedList.setProduct_quantity(10);
        updatedList.setProduct_price(-100f);
        listingSellRepository.save(updatedList);

        Iterable<ListingSell> listingSells = listingSellRepository.findAll();
        ListingSell retrievedList = listingSells.iterator().next();
        assertEquals(0, retrievedList.getProduct_price());
    }
}