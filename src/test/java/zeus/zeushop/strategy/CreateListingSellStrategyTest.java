package zeus.zeushop.strategy;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import zeus.zeushop.model.ListingSell;
import zeus.zeushop.repository.ListingSellRepository;

import static org.mockito.Mockito.*;

class CreateListingSellStrategyTest {

    @Mock
    private ListingSellRepository listingSellRepository;

    @InjectMocks
    private CreateListingSellStrategy createListingSellStrategy;

    private ListingSell listingSell;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        listingSell = new ListingSell();
    }

    @Test
    void testExecute() {
        createListingSellStrategy.execute(listingSell);

        verify(listingSellRepository).save(listingSell);
    }
}
