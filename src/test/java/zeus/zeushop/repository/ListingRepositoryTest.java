package zeus.zeushop.repository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import zeus.zeushop.model.Listing;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ListingRepositoryTest {

    @Mock
    private ListingRepository listingRepository;

    @InjectMocks
    private Listing listing;

    @Test
    void testFindAll_Positive() {
        List<Listing> listings = new ArrayList<>();
        // Mock behavior to return listings when findAll is called
        when(listingRepository.findAll()).thenReturn(listings);

        List<Listing> retrievedListings = listingRepository.findAll();

        // Assert that retrieved listings match the mocked listings
        assertEquals(listings, retrievedListings);
        // Verify that findAll method was called once
        verify(listingRepository, times(1)).findAll();
    }

    @Test
    void testFindById_Positive() {
        Integer id = 1;
        Listing listing = new Listing();
        when(listingRepository.findById(id)).thenReturn(Optional.of(listing));

        Optional<Listing> retrievedListing = listingRepository.findById(id);

        assertEquals(Optional.of(listing), retrievedListing);
        verify(listingRepository, times(1)).findById(id);
    }

    @Test
    void testFindAll_Negative() {
        // Mock behavior to return an empty list when findAll is called
        when(listingRepository.findAll()).thenReturn(new ArrayList<>());

        List<Listing> retrievedListings = listingRepository.findAll();

        // Assert that retrieved listings are empty
        assertEquals(0, retrievedListings.size());
        // Verify that findAll method was called once
        verify(listingRepository, times(1)).findAll();
    }

    @Test
    void testFindById_Negative() {
        Integer id = 1;
        when(listingRepository.findById(id)).thenReturn(Optional.empty());

        Optional<Listing> retrievedListing = listingRepository.findById(id);
        assertEquals(Optional.empty(), retrievedListing);

        verify(listingRepository, times(1)).findById(id);
    }

    @Test
    void testSaveListing_Positive() {
        Listing listing = new Listing();
        when(listingRepository.save(listing)).thenReturn(listing);

        Listing savedListing = listingRepository.save(listing);

        assertEquals(listing, savedListing);
        verify(listingRepository, times(1)).save(listing);
    }

    @Test
    void testSaveListing_Negative() {
        Listing listing = null; // Example of saving a null listing
        when(listingRepository.save(listing)).thenThrow(IllegalArgumentException.class);

        // Perform the save operation and catch the expected exception
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> listingRepository.save(listing));

        // Assert that the correct exception is thrown
        assertNotNull(exception);
        verify(listingRepository, times(1)).save(listing);
    }
}
