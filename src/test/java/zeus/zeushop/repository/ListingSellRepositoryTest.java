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
        listingSell.setId("ee8df7c4-036e-4137-bf44-2e9d10f6a191");
        listingSell.setName("Mini Skirt");
        listingSell.setDescription("Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.");
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
}