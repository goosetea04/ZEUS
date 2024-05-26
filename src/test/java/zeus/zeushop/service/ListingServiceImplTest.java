package zeus.zeushop.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import zeus.zeushop.model.Listing;
import zeus.zeushop.repository.ListingRepository;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
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

        Listing updatedListing = listingService.updateListing(id, listing);

        assertEquals(listing, updatedListing);
        verify(listingRepository, times(1)).findById(id);
        verify(listingRepository, times(1)).save(listing);
    }

    @Test
    void testUpdateListing_Negative() {
        Integer id = 11;
        Listing listing = new Listing();
        when(listingRepository.findById(id)).thenReturn(Optional.empty());

        Listing updatedListing = listingService.updateListing(id, listing);

        assertNull(updatedListing);
        verify(listingRepository, times(1)).findById(id);
        verify(listingRepository, never()).save(listing);
    }

    @Test
    void testDeleteListing_Positive() {
        Integer id = 1;
        doNothing().when(listingRepository).deleteById(id);

        assertDoesNotThrow(() -> listingService.deleteListing(id));
        verify(listingRepository, times(1)).deleteById(id);
    }

    @Test
    void testDeleteListing_Negative() {
        Integer id = 1;
        doThrow(RuntimeException.class).when(listingRepository).deleteById(id);

        assertThrows(RuntimeException.class, () -> listingService.deleteListing(id));
        verify(listingRepository, times(1)).deleteById(id);
    }

    @Test
    void testGetListingById_Positive() {
        Integer id = 1;
        Listing listing = new Listing();
        when(listingRepository.findById(id)).thenReturn(Optional.of(listing));

        Optional<Listing> result = listingService.getListingById(id);

        assertTrue(result.isPresent());
        assertEquals(listing, result.get());
        verify(listingRepository, times(1)).findById(id);
    }

    @Test
    void testGetListingById_Negative() {
        Integer id = 1;
        when(listingRepository.findById(id)).thenReturn(Optional.empty());

        Optional<Listing> result = listingService.getListingById(id);

        assertFalse(result.isPresent());
        verify(listingRepository, times(1)).findById(id);
    }

    @Test
    void testCreateListing() {
        Listing listing = new Listing();
        when(listingRepository.save(listing)).thenReturn(listing);

        Listing createdListing = listingService.createListing(listing);

        assertEquals(listing, createdListing);
        verify(listingRepository, times(1)).save(listing);
    }

    @Test
    void testGetListingsBySellerId() {
        Integer sellerId = 1;
        Listing listing1 = new Listing();
        listing1.setSeller_id(sellerId);
        Listing listing2 = new Listing();
        listing2.setSeller_id(sellerId);
        List<Listing> sellerListings = Arrays.asList(listing1, listing2);
        when(listingRepository.findBySellerId(sellerId)).thenReturn(sellerListings);

        List<Listing> result = listingService.getListingsBySellerId(sellerId);

        assertEquals(sellerListings, result);
        verify(listingRepository, times(1)).findBySellerId(sellerId);
    }

    @Test
    void testGetAllListings() {
        Listing listing1 = new Listing();
        Listing listing2 = new Listing();
        List<Listing> allListings = Arrays.asList(listing1, listing2);
        when(listingRepository.findAll()).thenReturn(allListings);

        List<Listing> result = listingService.getAllListings();

        assertEquals(allListings, result);
        verify(listingRepository, times(1)).findAll();
    }

    @Test
    public void testGetAllFeatured() {
        Listing listing1 = new Listing();
        Listing listing2 = new Listing();
        listing1.setEnd_date(LocalDateTime.now());
        listing2.setEnd_date(LocalDateTime.now());
        List<Listing> mockedListings = Arrays.asList(listing1, listing2);

        when(listingRepository.findByEndDateGreaterThan(any(LocalDateTime.class))).thenReturn(mockedListings);
        List<Listing> featuredListings = listingService.getAllFeatured();

        assertEquals(2, featuredListings.size());
    }
}
