package zeus.zeushop.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class ListingSellTest {
    ListingSell listingSell;

    @BeforeEach
    void setUp() {
        this.listingSell = new ListingSell();
        this.listingSell.setProduct_id(1);
        this.listingSell.setProduct_name("Mini Skirt");
        this.listingSell.setProduct_description("Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.");
        this.listingSell.setProduct_quantity(10);
        this.listingSell.setProduct_price(129000f);
        this.listingSell.setSellerId(123);
        this.listingSell.setVisible(true);
        this.listingSell.setEndDate(LocalDateTime.now().plusDays(1));
    }

    @Test
    void testDefaultConstructor() {
        ListingSell defaultListingSell = new ListingSell();
        assertNotNull(defaultListingSell);
    }

    @Test
    void testParameterizedConstructor() {
        ListingSell paramListingSell = new ListingSell("Product", LocalDateTime.now().plusDays(2));
        assertNotNull(paramListingSell);
        assertEquals("Product", paramListingSell.getProduct_name());
        assertEquals(LocalDateTime.now().plusDays(2).withNano(0), paramListingSell.getEndDate().withNano(0));
    }

    @Test
    void testGetListingSellId() {
        assertEquals(1, this.listingSell.getProduct_id());
    }

    @Test
    void testGetListingSellName() {
        assertEquals("Mini Skirt", this.listingSell.getProduct_name());
    }

    @Test
    void testGetListingSellDescription() {
        assertEquals("Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.", this.listingSell.getProduct_description());
    }

    @Test
    void testGetListingSellStock() {
        assertEquals(10, this.listingSell.getProduct_quantity());
    }

    @Test
    void testGetListingSellPrice() {
        assertEquals(129000f, this.listingSell.getProduct_price());
    }

    @Test
    void testGetSellerId() {
        assertEquals(123, this.listingSell.getSellerId());
    }

    @Test
    void testIsVisible() {
        assertTrue(this.listingSell.isVisible());
    }

    @Test
    void testIsFeatured() {
        assertTrue(this.listingSell.isFeatured());
    }

    @Test
    void testIsFeaturedWhenEndDateIsNull() {
        this.listingSell.setEndDate(null);
        assertFalse(this.listingSell.isFeatured());
    }

    @Test
    void testIsFeaturedWhenEndDateIsPast() {
        this.listingSell.setEndDate(LocalDateTime.now().minusDays(1));
        assertFalse(this.listingSell.isFeatured());
    }

    @Test
    void testSetVisible() {
        this.listingSell.setVisible(false);
        assertFalse(this.listingSell.isVisible());
        this.listingSell.setVisible(true);
        assertTrue(this.listingSell.isVisible());
    }

    @Test
    void testSetSellerId() {
        this.listingSell.setSellerId(456);
        assertEquals(456, this.listingSell.getSellerId());
    }

    @Test
    void testSetProductId() {
        this.listingSell.setProduct_id(2);
        assertEquals(2, this.listingSell.getProduct_id());
    }

    @Test
    void testSetProductName() {
        this.listingSell.setProduct_name("T-shirt");
        assertEquals("T-shirt", this.listingSell.getProduct_name());
    }

    @Test
    void testSetProductDescription() {
        this.listingSell.setProduct_description("New description");
        assertEquals("New description", this.listingSell.getProduct_description());
    }

    @Test
    void testSetProductQuantity() {
        this.listingSell.setProduct_quantity(20);
        assertEquals(20, this.listingSell.getProduct_quantity());
    }

    @Test
    void testSetProductPrice() {
        this.listingSell.setProduct_price(159000f);
        assertEquals(159000f, this.listingSell.getProduct_price());
    }

    @Test
    void testSetEndDate() {
        LocalDateTime newEndDate = LocalDateTime.now().plusDays(3);
        this.listingSell.setEndDate(newEndDate);
        assertEquals(newEndDate, this.listingSell.getEndDate());
    }

    @Test
    void testGetEndDate() {
        assertEquals(LocalDateTime.now().plusDays(1).withNano(0), this.listingSell.getEndDate().withNano(0));
    }
}

