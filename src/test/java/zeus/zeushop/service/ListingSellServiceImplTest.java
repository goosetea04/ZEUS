package zeus.zeushop.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import zeus.zeushop.model.ListingSell;
import zeus.zeushop.repository.ListingSellRepository;

class ListingSellServiceImplTest {

    @Mock
    private ListingSellRepository listingSellRepository;

    @InjectMocks
    private ListingSellServiceImpl listingSellService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateListingSell() {
//        String id = UUID.randomUUID().toString();

        ListingSell list = new ListingSell();
        list.setProduct_id(1);
        list.setProduct_name("Mini Skirt");
        list.setProduct_description("Lorem ipsum dolor sit amet,");
        list.setProduct_quantity(10);
        list.setProduct_price(129000f);

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

//    @Test
//    void testFindListingSellById() {
////        String id = UUID.randomUUID().toString();
//
//        ListingSell list = new ListingSell();
//        list.setProduct_id(1);
//        list.setProduct_name("Mini Skirt");
//        list.setProduct_description("Lorem ipsum dolor sit amet,");
//        list.setProduct_quantity(10);
//        list.setProduct_price(129000f);
//        list.setProduct_id(1);
//
//        when(listingSellRepository.findById(1)).thenReturn(java.util.Optional.of(list));
//        ListingSell foundList = listingSellService.findById(1);
//
//        assertNotNull(foundList);
//        assertEquals(1, foundList.getProduct_id());
//    }

    @Test
    void testEditListingSell() {
//        String id = UUID.randomUUID().toString();

        ListingSell list = new ListingSell();
        list.setProduct_id(1);
        list.setProduct_name("Mini Skirt");
        list.setProduct_description("Lorem ipsum dolor sit amet,");
        list.setProduct_quantity(10);
        list.setProduct_price(129000f);

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
//        String id = UUID.randomUUID().toString();

        ListingSell list = new ListingSell();
        list.setProduct_id(1);
        list.setProduct_name("Mini Skirt");
        list.setProduct_description("Lorem ipsum dolor sit amet,");
        list.setProduct_quantity(10);
        list.setProduct_price(129000f);

        when(listingSellRepository.findById(1)).thenReturn(java.util.Optional.of(list));
        ListingSell deletedList = listingSellService.deleteListingSell(1);

        assertNotNull(deletedList);
        assertEquals(1, deletedList.getProduct_id());
    }
}