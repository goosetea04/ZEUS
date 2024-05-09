package zeus.zeushop.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

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
        assertEquals(129000, this.listingSell.getProduct_price());
    }
}
