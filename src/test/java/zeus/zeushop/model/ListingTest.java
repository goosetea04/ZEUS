package zeus.zeushop.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import java.time.LocalDateTime;

public class ListingTest {

    @Test
    public void testListingConstructor() {
        Listing listing = new Listing();

        assertNotNull(listing);
    }

    @Test
    public void testListingSetterAndGetter() {
        Listing listing = new Listing();
        listing.setProduct_id(123);
        listing.setProduct_name("Test Listing");
        listing.setProduct_description("Description of Test Listing");
        listing.setProduct_quantity(20);
        listing.setProduct_price(50.0f);

        assertEquals(123, listing.getProduct_id());
        assertEquals("Test Listing", listing.getProduct_name());
        assertEquals("Description of Test Listing", listing.getProduct_description());
        assertEquals(20, listing.getProduct_quantity());
        assertEquals(50.0f, listing.getProduct_price());
    }

    @Test
    public void testIsFeatured() {
        Listing listing = new Listing();
        listing.setEndDate(null);
        assertFalse(listing.isFeatured());

        // Create a future date from now
        LocalDateTime futureDate = LocalDateTime.now().plusDays(1);
        listing.setEndDate(futureDate);
        assertTrue(listing.isFeatured());

        // Create a past date from now
        LocalDateTime pastDate = LocalDateTime.now().minusDays(1);
        listing.setEndDate(pastDate);
        assertFalse(listing.isFeatured());
    }

    @Test
    public void testNullFields() {
        Listing listing = new Listing();
        assertNull(listing.getProduct_name());
        assertNull(listing.getProduct_description());
        assertNull(listing.getSeller_id());
        assertNull(listing.getEndDate());
    }

    @Test
    public void testNegativePrice() {
        Listing listing = new Listing();
        listing.setProduct_price(-10.0f);
        assertEquals(-10.0f, listing.getProduct_price());
    }

    @Test
    public void testNegativeQuantity() {
        Listing listing = new Listing();
        listing.setProduct_quantity(-5);
        assertEquals(-5, listing.getProduct_quantity());
    }

    @Test
    public void testGetSellerId() {
        Listing listing = new Listing();
        listing.setSeller_id(100);
        assertEquals(100, listing.getSeller_id());
    }

    @Test
    public void testIsVisible() {
        Listing listing = new Listing();
        listing.setVisible(true);
        assertTrue(listing.isVisible());

        listing.setVisible(false);
        assertFalse(listing.isVisible());
    }

    @Test
    public void testGetId() {
        Listing listing = new Listing();
        listing.setProduct_id(999);
        assertEquals(999, listing.getId());
    }

    @Test
    public void testSetSellerId() {
        Listing listing = new Listing();
        listing.setSeller_id(250);
        assertEquals(250, listing.getSeller_id());
    }

    @Test
    public void testSetVisible() {
        Listing listing = new Listing();
        listing.setVisible(true);
        assertTrue(listing.isVisible());

        listing.setVisible(false);
        assertFalse(listing.isVisible());
    }

    @Test
    public void testConstructorWithStringAndLocalDateTime() {
        LocalDateTime endDate = LocalDateTime.now().plusDays(10);
        Listing listing = new Listing("Test Product", endDate);

        assertEquals("Test Product", listing.getProduct_name());
        assertEquals(endDate, listing.getEndDate());
    }

    @Test
    public void testConstructorWithLongAndOtherParameters() {
        Listing listing = new Listing(1L, "Test Product", 10, "Product Description", 99.99, 1L, true);

        assertEquals(1, listing.getProduct_id());
        assertEquals("Test Product", listing.getProduct_name());
        assertEquals(10, listing.getProduct_quantity());
        assertEquals("Product Description", listing.getProduct_description());
        assertEquals(99.99f, listing.getProduct_price());
        assertEquals(1, listing.getSeller_id());
        assertTrue(listing.isVisible());
    }
    @Test
    public void testSetId() {
        Listing listing = new Listing();
        listing.setId(500);
        assertEquals(500, listing.getId());
    }
}
