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

        String redirect = listingController.saveListing(listing);

        assertEquals("redirect:/listings", redirect);
        verify(listingService).createListing(listing);
    }

    @Test
    void testSaveListing_Negative() {
        Listing listing = new Listing();
        when(listingService.createListing(listing)).thenReturn(null);

        String redirect = listingController.saveListing(listing);

        assertEquals("redirect:/listings", redirect); // or any other error handling behavior
        verify(listingService).createListing(listing);
    }
}
