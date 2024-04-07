package zeus.zeushop.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ListingSellTest {
    ListingSell listingSell;
    @BeforeEach
    void SetUp() {
        this.listingSell = new ListingSell();
        this.listingSell.setId("ee8df7c4-036e-4137-bf44-2e9d10f6a191");
        this.listingSell.setName("Mini Skirt");
        this.listingSell.setDescription("Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.");
        this.listingSell.setStock(10);
        this.listingSell.setPrice(129000);
    }

    @Test
    void testGetListingSellId() {
        assertEquals ( "ee8df7c4-036e-4137-bf44-2e9d10f6a191", this.listingSell.getId());
    }

    @Test
    void testGetListingSellName() {
        assertEquals( "Mini Skirt", this.listingSell.getName());
    }

    @Test
    void testGetListingSellDescription() {
        assertEquals( "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.", this.listingSell.getDescription());
    }

    @Test
    void testGetListingSellStock() {
        assertEquals( 10, this.listingSell.getStock());
    }

    @Test
    void testGetListingSellPrice() {
        assertEquals( 129000, this.listingSell.getPrice());
    }
}