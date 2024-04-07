package zeus.zeushop.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import zeus.zeushop.model.Listing;

import java.util.Iterator;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ListingRepositoryTest {

    private ListingRepository listingRepository;

    @BeforeEach
    public void setUp() {
        listingRepository = new ListingRepository();
    }

    @Test
    public void testCreateListing() {
        Listing listing = new Listing();
        listing.setListingID("1");
        listing.setListingName("Test Listing");
        listing.setListingDescription("Description of Test Listing");
        listing.setListingStock(20);
        listing.setListingPrice(50);

        Listing createdListing = listingRepository.create(listing);

        assertEquals(listing, createdListing);
    }

    @Test
    public void testFindAllListings() {
        Listing listing1 = new Listing();
        listing1.setListingID("1");
        listing1.setListingName("Test Listing 1");
        listing1.setListingDescription("Description of Test Listing 1");
        listing1.setListingStock(20);
        listing1.setListingPrice(50);

        Listing listing2 = new Listing();
        listing2.setListingID("2");
        listing2.setListingName("Test Listing 2");
        listing2.setListingDescription("Description of Test Listing 2");
        listing2.setListingStock(15);
        listing2.setListingPrice(30);

        listingRepository.create(listing1);
        listingRepository.create(listing2);

        Iterator<Listing> iterator = listingRepository.findAll();

        assertTrue(iterator.hasNext());

        Listing retrievedListing1 = iterator.next();
        Listing retrievedListing2 = iterator.next();

        assertEquals(listing1, retrievedListing1);
        assertEquals(listing2, retrievedListing2);

        // Ensure there are no more listings
        assertTrue(!iterator.hasNext());
    }
}
