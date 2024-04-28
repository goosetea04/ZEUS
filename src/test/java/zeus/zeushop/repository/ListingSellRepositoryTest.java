package zeus.zeushop.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import zeus.zeushop.model.ListingSell;

import java.util.Iterator;
import java.util.UUID;

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
        listingSell.setId("ee8df7c4-036e-4137-bf44-2e9d10f6a191");
        listingSell.setName("Mini Skirt");
        listingSell.setDescription("Lorem ipsum dolor sit amet,");
        listingSell.setStock(10);
        listingSell.setPrice(129000);
        listingSellRepository.create(listingSell);

        Iterator<ListingSell> listingSellIterator = listingSellRepository.findAll();
        assertTrue(listingSellIterator.hasNext());
        ListingSell savedListingSell = listingSellIterator.next();
        assertEquals(listingSell.getId(), savedListingSell.getId());
        assertEquals(listingSell.getName(), savedListingSell.getName());
        assertEquals(listingSell.getDescription(), savedListingSell.getDescription());
        assertEquals(listingSell.getStock(), savedListingSell.getStock());
        assertEquals(listingSell.getPrice(), savedListingSell.getPrice());
    }

    @Test
    void testFindAllIfEmpty() {
        Iterator<ListingSell> listingSellIterator = listingSellRepository.findAll();
        assertFalse(listingSellIterator.hasNext());
    }

    @Test
    void testFindById() {
        String id = UUID.randomUUID().toString();

        ListingSell list = new ListingSell();
        list.setId(id);
        list.setName("Mini Skirt");
        list.setDescription("Lorem ipsum dolor sit amet,");
        list.setStock(10);
        list.setPrice(129000);
        listingSellRepository.create(list);

        ListingSell returnedListingSell = listingSellRepository.findById(id);
        assertNotNull(returnedListingSell);
    }

    @Test
    void testFindByIdNonexisting() {
        String id1 = UUID.randomUUID().toString();
        String id2 = UUID.randomUUID().toString();

        ListingSell list = new ListingSell();
        list.setId(id1);
        list.setName("Mini Skirt");
        list.setDescription("Lorem ipsum dolor sit amet,");
        list.setStock(10);
        list.setPrice(129000);
        listingSellRepository.create(list);

        ListingSell returnedListingSell = listingSellRepository.findById(id2);
        assertNotNull(returnedListingSell);
    }

    @Test
    void testFindAllIfMoreThanOneList() {
        String id1 = UUID.randomUUID().toString();
        ListingSell list1 = new ListingSell();
        list1.setId(id1);
        list1.setName("Mini Skirt");
        list1.setDescription("Lorem ipsum dolor sit amet,");
        list1.setStock(10);
        list1.setPrice(129000);
        listingSellRepository.create(list1);

        String id2 = UUID.randomUUID().toString();
        ListingSell list2 = new ListingSell();
        list2.setId(id2);
        list2.setName("White Shirt");
        list2.setDescription("Lorem ipsum dolor sit amet,");
        list2.setStock(5);
        list2.setPrice(99000);
        listingSellRepository.create(list2);

        Iterator<ListingSell> listingSellIterator = listingSellRepository.findAll();
        assertTrue(listingSellIterator.hasNext());
        ListingSell savedListingSell = listingSellIterator.next();
        assertEquals(list1.getId(), savedListingSell.getId());
        savedListingSell = listingSellIterator.next();
        assertEquals(list2.getId(), savedListingSell.getId());
    }

    @Test
    void testCreateUpdateThenFind() {
        String id = UUID.randomUUID().toString();

        ListingSell list = new ListingSell();
        list.setId(id);
        list.setName("Mini Skirt");
        list.setDescription("Lorem ipsum dolor sit amet,");
        list.setStock(10);
        list.setPrice(129000);
        listingSellRepository.create(list);

        ListingSell updatedList = new ListingSell();
        updatedList.setId(id);
        updatedList.setName("Black Shirt");
        updatedList.setDescription("Lorem ipsum dolor sit amet,");
        updatedList.setStock(10);
        updatedList.setPrice(119000);
        listingSellRepository.editListingSell(updatedList);

        assertEquals(list.getName(), updatedList.getName());
        assertEquals(list.getDescription(), updatedList.getDescription());
        assertEquals(list.getStock(), updatedList.getStock());
        assertEquals(list.getPrice(), updatedList.getPrice());
    }

    @Test
    void testUpdateStockNotNegative() {
        String id = UUID.randomUUID().toString();

        ListingSell list = new ListingSell();
        list.setId(id);
        list.setName("Mini Skirt");
        list.setDescription("Lorem ipsum dolor sit amet,");
        list.setStock(10);
        list.setPrice(129000);
        listingSellRepository.create(list);

        ListingSell updatedList = new ListingSell();
        updatedList.setId(id);
        updatedList.setName("Black Shirt");
        updatedList.setDescription("Lorem ipsum dolor sit amet,");
        updatedList.setStock(-1);
        updatedList.setPrice(119000);
        listingSellRepository.editListingSell(updatedList);

        assertEquals(0, list.getStock());
    }

    @Test
    void testUpdatePriceNotNegative() {
        String id = UUID.randomUUID().toString();

        ListingSell list = new ListingSell();
        list.setId(id);
        list.setName("Mini Skirt");
        list.setDescription("Lorem ipsum dolor sit amet,");
        list.setStock(10);
        list.setPrice(129000);
        listingSellRepository.create(list);

        ListingSell updatedList = new ListingSell();
        updatedList.setId(id);
        updatedList.setName("Black Shirt");
        updatedList.setDescription("Lorem ipsum dolor sit amet,");
        updatedList.setStock(10);
        updatedList.setPrice(-100);
        listingSellRepository.editListingSell(updatedList);

        assertEquals(0, list.getPrice());
    }

    @Test
    void testUpdateNonexistingList() {
        String id1 = UUID.randomUUID().toString();
        String id2 = UUID.randomUUID().toString();

        ListingSell list = new ListingSell();
        list.setId(id1);
        list.setName("Mini Skirt");
        list.setDescription("Lorem ipsum dolor sit amet,");
        list.setStock(10);
        list.setPrice(129000);
        listingSellRepository.create(list);

        ListingSell updatedNonexisting = new ListingSell();
        updatedNonexisting.setId(id2);
        updatedNonexisting.setName("White Shirt");
        updatedNonexisting.setStock(100);
        updatedNonexisting.setPrice(119000);
        ListingSell returnedList = listingSellRepository.editListingSell(updatedNonexisting);

        assertNull(returnedList);
    }

    @Test
    void testCreateDeleteThenFind() {
        String id = UUID.randomUUID().toString();

        ListingSell list = new ListingSell();
        list.setId(id);
        list.setName("Mini Skirt");
        list.setDescription("Lorem ipsum dolor sit amet,");
        list.setStock(10);
        list.setPrice(129000);
        listingSellRepository.create(list);

        Iterator<ListingSell> listingSellIterator = listingSellRepository.findAll();
        assertTrue(listingSellIterator.hasNext());
        listingSellRepository.deleteListingSell(id);
        listingSellIterator = listingSellRepository.findAll();
        assertFalse(listingSellIterator.hasNext());
    }

    @Test
    void testDeleteNonexisting() {
        String id1 = UUID.randomUUID().toString();
        String id2 = UUID.randomUUID().toString();

        ListingSell list = new ListingSell();
        list.setId(id1);
        list.setName("Mini Skirt");
        list.setDescription("Lorem ipsum dolor sit amet,");
        list.setStock(10);
        list.setPrice(129000);
        listingSellRepository.create(list);

        ListingSell deletedList = listingSellRepository.deleteListingSell(id2);
        assertNull(deletedList);
    }
}