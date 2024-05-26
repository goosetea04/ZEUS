package zeus.zeushop.strategy;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import zeus.zeushop.model.ListingSell;
import zeus.zeushop.repository.ListingSellRepository;

import java.util.Optional;

import static org.mockito.Mockito.*;

class DeleteListingSellStrategyTest {

    @Mock
    private ListingSellRepository listingSellRepository;

    @InjectMocks
    private DeleteListingSellStrategy deleteListingSellStrategy;

    private ListingSell listingSell;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        listingSell = new ListingSell();
        listingSell.setProduct_id(1);
    }

    @Test
    void testExecute() {
        when(listingSellRepository.findById(1)).thenReturn(Optional.of(listingSell));

        deleteListingSellStrategy.execute(listingSell);

        verify(listingSellRepository).findById(1);
        verify(listingSellRepository).delete(listingSell);
    }
}
