package zeus.zeushop.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class ListingTest {

    @Test
    public void testListingConstructor() {
        Listing listing = new Listing();

        assertNotNull(listing);
    }

    @Test
    public void testListingSetterAndGetter() {
        Listing listing = new Listing();
        listing.setListingID("123");
        listing.setListingName("Test Listing");
        listing.setListingDescription("Description of Test Listing");
        listing.setListingStock(20);
        listing.setListingPrice(50);

        assertEquals("123", listing.getListingID());
        assertEquals("Test Listing", listing.getListingName());
        assertEquals("Description of Test Listing", listing.getListingDescription());
        assertEquals(20, listing.getListingStock());
        assertEquals(50, listing.getListingPrice());
    }
}
