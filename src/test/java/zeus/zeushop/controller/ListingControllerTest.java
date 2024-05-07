package zeus.zeushop.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.Model;
import zeus.zeushop.model.Listing;
import zeus.zeushop.service.ListingService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ListingControllerTest {

    @Mock
    private ListingService listingService;

    @InjectMocks
    private ListingController listingController;

    private Model model;

    @BeforeEach
    void setUp() {
        model = mock(Model.class);
    }

    @Test
    void testShowAddListingForm() {
        String viewName = listingController.showAddListingForm(model);
        assertEquals("add-listing", viewName);
        verify(model).addAttribute(eq("listing"), any(Listing.class));
    }

    @Test
    void testSaveListing_Positive() {
        Listing listing = new Listing();
        when(listingService.createListing(listing)).thenReturn(listing);

        String redirect = listingController.saveListing(listing, model);

        assertEquals("redirect:/listings", redirect);
        verify(listingService).createListing(listing);
    }

    @Test
    void testSaveListing_Negative() {
        Listing listing = new Listing();
        when(listingService.createListing(listing)).thenReturn(null);

        String redirect = listingController.saveListing(listing, model);

        assertEquals("redirect:/listings", redirect); // or any other error handling behavior
        verify(listingService).createListing(listing);
    }

    @Test
    void testGetAllListings() {
        // Mock data
        List<Listing> listings = new ArrayList<>();
        listings.add(new Listing());
        listings.add(new Listing());

        when(listingService.getAllListings()).thenReturn(listings);

        String viewName = listingController.getAllListings(model);

        assertEquals("listings", viewName);
        verify(model).addAttribute("listings", listings);
    }

    @Test
    void testGetAllListings_Empty() {
        when(listingService.getAllListings()).thenReturn(new ArrayList<>());

        String viewName = listingController.getAllListings(model);

        assertEquals("listings", viewName);
        verify(model).addAttribute("listings", new ArrayList<>());
    }

    @Test
    void testGetAllListings_Null() {
        when(listingService.getAllListings()).thenReturn(null);

        String viewName = listingController.getAllListings(model);

        assertEquals("listings", viewName);
        verify(model).addAttribute("listings", new ArrayList<>());
    }

    @Test
    void testShowUpdateListingForm() {
        Integer id = 1;
        Listing listing = new Listing();
        when(listingService.getListingById(id.longValue())).thenReturn(Optional.of(listing));

        String viewName = listingController.showUpdateListingForm(id, model);

        assertEquals("update-listing", viewName);
        verify(model).addAttribute("listing", listing);
    }



    @Test
    void testShowUpdateListingForm_NotFound() {
        Long id = 1L; // Use Long instead of Integer
        when(listingService.getListingById(id)).thenReturn(Optional.empty());

        String redirect = listingController.showUpdateListingForm(id.intValue(), model); // Convert Long to int

        assertEquals("redirect:/listings", redirect);
        verify(listingService).getListingById(id);
    }


    @Test
    void testUpdateListing() {
        Long id = 1L;
        Listing updatedListing = new Listing();
        when(listingService.updateListing(id, updatedListing)).thenReturn(updatedListing);

        String redirect = listingController.updateListing(updatedListing, id, model);

        assertEquals("redirect:/listings", redirect);
        verify(listingService).updateListing(id, updatedListing);
    }

    @Test
    void testUpdateListing_NotFound() {
        Long id = 1L;
        Listing updatedListing = new Listing();
        when(listingService.updateListing(id, updatedListing)).thenReturn(null);

        String redirect = listingController.updateListing(updatedListing, id, model);

        assertEquals("redirect:/listings", redirect);
        verify(listingService).updateListing(id, updatedListing);
    }

    @Test
    void testDeleteListing() {
        Long id = 1L;

        String redirect = listingController.deleteListing(id);

        assertEquals("redirect:/manage-listings", redirect);
        verify(listingService).deleteListing(id);
    }
    @Test
    void testDeleteListing_Exception() {
        Long id = 1L;
        // Simulate an exception being thrown
        doThrow(new RuntimeException()).when(listingService).deleteListing(id);

        String redirect = listingController.deleteListing(id);

        assertEquals("redirect:/listings", redirect);
        verify(listingService).deleteListing(id);
    }
}
