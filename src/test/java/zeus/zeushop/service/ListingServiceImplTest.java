package zeus.zeushop.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import zeus.zeushop.model.Listing;
import zeus.zeushop.repository.ListingRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ListingServiceImplTest {

    @Mock
    private ListingRepository listingRepository;

    @InjectMocks
    private ListingServiceImpl listingService;

    @Test
    void testUpdateListing_Positive() {
        Integer id = 1;
        Listing listing = new Listing();
        listing.setProduct_name("Test Product");
        when(listingRepository.findById(id)).thenReturn(Optional.of(listing));
        when(listingRepository.save(listing)).thenReturn(listing);

        Listing updatedListing = listingService.updateListing(id.longValue(), listing);

        assertEquals(listing, updatedListing);
        verify(listingRepository, times(1)).findById(id);
        verify(listingRepository, times(1)).save(listing);
    }

    @Test
    void testUpdateListing_Negative() {
        Integer id = 11;
        Listing listing = new Listing();
        when(listingRepository.findById(id)).thenReturn(Optional.empty());

        Listing updatedListing = listingService.updateListing(id.longValue(), listing);

        assertNull(updatedListing);
        verify(listingRepository, times(1)).findById(id);
        verify(listingRepository, never()).save(listing);
    }

    @Test
    void testDeleteListing_Positive() {
        Integer id = 1;
        doNothing().when(listingRepository).deleteById(id);

        assertDoesNotThrow(() -> listingService.deleteListing(id.longValue()));
        verify(listingRepository, times(1)).deleteById(id);
    }

    @Test
    void testDeleteListing_Negative() {
        Integer id = 1;
        doThrow(RuntimeException.class).when(listingRepository).deleteById(id);

        assertThrows(RuntimeException.class, () -> listingService.deleteListing(id.longValue()));
        verify(listingRepository, times(1)).deleteById(id);
    }
}
