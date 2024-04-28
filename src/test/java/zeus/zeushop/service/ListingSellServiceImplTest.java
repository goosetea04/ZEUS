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
    void testCreateList() {
        String id = UUID.randomUUID().toString();

        ListingSell list = new ListingSell();
        list.setId(id);
        list.setName("Mini Skirt");
        list.setDescription("Lorem ipsum dolor sit amet,");
        list.setStock(10);
        list.setPrice(129000);

        when(listingSellRepository.create(list)).thenReturn(list);
        ListingSell createdList = listingSellService.create(list);

        assertEquals("Test List", createdList.getName());
        assertEquals("Lorem ipsum dolor sit amet,", createdList.getDescription());
        assertEquals(10, createdList.getStock());
        assertEquals(129000, createdList.getPrice());
        assertEquals(id, createdList.getId());
    }

    @Test
    void testCreateAndFindAll() {
        List<ListingSell> listingSells = new ArrayList<>();
        ListingSell list1 = new ListingSell();
        ListingSell list2 = new ListingSell();
        listingSells.add(list1);
        listingSells.add(list2);

        when(listingSellRepository.findAll()).thenReturn(listingSells.iterator());
        List<ListingSell> foundLists = listingSellService.findAll();

        assertEquals(2, foundLists.size());
    }

    @Test
    void testGetListById() {
        String id = UUID.randomUUID().toString();

        ListingSell list = new ListingSell();
        list.setId(id);

        when(listingSellRepository.findById(id)).thenReturn(list);
        ListingSell foundList = listingSellService.findById(id);

        assertNotNull(foundList);
        assertEquals(id, foundList.getId());
    }

    @Test
    void testEditList() {
        String id = UUID.randomUUID().toString();

        ListingSell list = new ListingSell();
        list.setId(id);
        list.setName("Mini Skirt");
        list.setDescription("Lorem ipsum dolor sit amet,");
        list.setStock(10);
        list.setPrice(129000);

        when(listingSellRepository.editListingSell(list)).thenReturn(list);
        ListingSell editedList = listingSellRepository.editListingSell(list);

        assertEquals("Mini Skirt", editedList.getName());
        assertEquals("Lorem ipsum dolor sit amet,", editedList.getDescription());
        assertEquals(10, editedList.getStock());
        assertEquals(129000, editedList.getPrice());
    }

    @Test
    void testDeleteList() {
        String id = UUID.randomUUID().toString();

        ListingSell list = new ListingSell();
        list.setId(id);
        list.setName("Mini Skirt");
        list.setDescription("Lorem ipsum dolor sit amet,");
        list.setStock(10);
        list.setPrice(129000);

        when(listingSellRepository.deleteListingSell(id)).thenReturn(list);
        ListingSell deletedList = listingSellService.deleteListingSell(id);

        assertNotNull(deletedList);
        assertEquals(id, deletedList.getId());
    }
}