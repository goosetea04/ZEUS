package zeus.zeushop.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import org.springframework.beans.factory.annotation.Qualifier;
import zeus.zeushop.model.ListingSell;
import zeus.zeushop.repository.ListingSellRepository;
import zeus.zeushop.strategy.ListingSellStrategy;

class ListingSellServiceImplTest {

    @Mock
    private ListingSellRepository listingSellRepository;

    @Mock
    @Qualifier("createListingSellStrategy")
    private ListingSellStrategy createStrategy;

    @Mock
    @Qualifier("editListingSellStrategy")
    private ListingSellStrategy editStrategy;

    @Mock
    @Qualifier("deleteListingSellStrategy")
    private ListingSellStrategy deleteStrategy;

    @InjectMocks
    private ListingSellServiceImpl listingSellService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateListingSell() {
        ListingSell list = new ListingSell();
        list.setProduct_id(1);
        list.setProduct_name("Mini Skirt");
        list.setProduct_description("Lorem ipsum dolor sit amet,");
        list.setProduct_quantity(10);
        list.setProduct_price(129000f);

        doNothing().when(createStrategy).execute(list);
        when(listingSellRepository.save(list)).thenReturn(list);
        ListingSell createdList = listingSellService.create(list);

        assertEquals("Mini Skirt", createdList.getProduct_name());
        assertEquals("Lorem ipsum dolor sit amet,", createdList.getProduct_description());
        assertEquals(10, createdList.getProduct_quantity());
        assertEquals(129000f, createdList.getProduct_price());
        assertEquals(1, createdList.getProduct_id());
    }

    @Test
    void testFindAllListingSells() {
        List<ListingSell> listingSells = new ArrayList<>();
        ListingSell list1 = new ListingSell();
        ListingSell list2 = new ListingSell();
        listingSells.add(list1);
        listingSells.add(list2);

        when(listingSellRepository.findAll()).thenReturn(listingSells);
        List<ListingSell> foundLists = listingSellService.findAll();

        assertEquals(2, foundLists.size());
    }

    @Test
    void testFindListingSellById() {
        ListingSell list = new ListingSell();
        list.setProduct_id(1);
        list.setProduct_name("Mini Skirt");
        list.setProduct_description("Lorem ipsum dolor sit amet,");
        list.setProduct_quantity(10);
        list.setProduct_price(129000f);

        when(listingSellRepository.findById(1)).thenReturn(Optional.of(list));
        Optional<ListingSell> foundList = listingSellService.findById(1);

        assertTrue(foundList.isPresent());
        assertEquals(1, foundList.get().getProduct_id());
    }

    @Test
    void testEditListingSell() {
        ListingSell list = new ListingSell();
        list.setProduct_id(1);
        list.setProduct_name("Mini Skirt");
        list.setProduct_description("Lorem ipsum dolor sit amet,");
        list.setProduct_quantity(10);
        list.setProduct_price(129000f);

        doNothing().when(editStrategy).execute(list);
        when(listingSellRepository.save(list)).thenReturn(list);
        ListingSell editedList = listingSellService.editListingSell(1, list);

        assertEquals("Mini Skirt", editedList.getProduct_name());
        assertEquals("Lorem ipsum dolor sit amet,", editedList.getProduct_description());
        assertEquals(10, editedList.getProduct_quantity());
        assertEquals(129000f, editedList.getProduct_price());
        assertEquals(1, editedList.getProduct_id());
    }

    @Test
    void testDeleteListingSell() {
        ListingSell list = new ListingSell();
        list.setProduct_id(1);
        list.setProduct_name("Mini Skirt");
        list.setProduct_description("Lorem ipsum dolor sit amet,");
        list.setProduct_quantity(10);
        list.setProduct_price(129000f);

        when(listingSellRepository.findById(1)).thenReturn(Optional.of(list));
        doNothing().when(deleteStrategy).execute(list);
        ListingSell deletedList = listingSellService.deleteListingSell(1);

        assertNotNull(deletedList);
        assertEquals(1, deletedList.getProduct_id());
    }

    @Test
    void testDeleteNonExistingListingSell() {
        when(listingSellRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> {
            listingSellService.deleteListingSell(1);
        });
    }

    @Test
    void testFindBySellerId() {
        List<ListingSell> listingSells = new ArrayList<>();
        ListingSell list1 = new ListingSell();
        list1.setSellerId(123);
        ListingSell list2 = new ListingSell();
        list2.setSellerId(123);
        listingSells.add(list1);
        listingSells.add(list2);

        when(listingSellRepository.findBySellerId(123)).thenReturn(listingSells);
        List<ListingSell> foundLists = listingSellService.findBySellerId(123);

        assertEquals(2, foundLists.size());
    }

    @Test
    void testFindBySellerIdWithNoListings() {
        when(listingSellRepository.findBySellerId(999)).thenReturn(new ArrayList<>());

        List<ListingSell> foundLists = listingSellService.findBySellerId(999);

        assertTrue(foundLists.isEmpty());
    }

    @Test
    void testEditNonExistingListingSell() {
        ListingSell list = new ListingSell();
        list.setProduct_id(1);
        list.setProduct_name("Mini Skirt");
        list.setProduct_description("Lorem ipsum dolor sit amet,");
        list.setProduct_quantity(10);
        list.setProduct_price(129000f);

        when(listingSellRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> {
            listingSellService.editListingSell(1, list);
        });
    }
}