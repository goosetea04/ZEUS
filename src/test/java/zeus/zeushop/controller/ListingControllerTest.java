package zeus.zeushop.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import zeus.zeushop.service.PaymentService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;
import org.springframework.validation.support.BindingAwareModelMap;
import zeus.zeushop.model.*;
import zeus.zeushop.service.ListingService;
import zeus.zeushop.service.ShoppingCartService;
import zeus.zeushop.service.UserService;
import zeus.zeushop.repository.ListingRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;


public class ListingControllerTest {

    @Mock
    private UserService userService;

    @Mock
    private ShoppingCartService shoppingCartService;

    @Mock
    private ListingService listingService;

    @Mock
    private PaymentService paymentService;

    @InjectMocks
    private CartController cartController;

    @Mock
    private ListingRepository listingRepository;

    @InjectMocks
    private ListingController listingController;

    @Mock
    private Authentication authentication;

    @Mock
    private SecurityContext securityContext;

    private Model model;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        model = new BindingAwareModelMap();
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
    }

    @Test
    public void testGetAllListings() {
        User currentUser = new User();
        currentUser.setId(1);
        when(authentication.getName()).thenReturn("user");
        when(userService.getUserByUsername("user")).thenReturn(currentUser);

        Listing listing1 = new Listing();
        listing1.setVisible(true);
        listing1.setSellerId(2);

        Listing listing2 = new Listing();
        listing2.setVisible(true);
        listing2.setSellerId(3);

        List<Listing> listings = new ArrayList<>();
        listings.add(listing1);
        listings.add(listing2);

        when(listingService.getAllListings()).thenReturn(listings);

        String viewName = listingController.getAllListings(model);

        assertEquals("listings", viewName);
        List<Listing> visibleListings = (List<Listing>) model.getAttribute("listings");
        assertNotNull(visibleListings);
        assertEquals(2, visibleListings.size());
    }

    @Test
    public void testAddToCart() {
        User currentUser = new User();
        currentUser.setId(1);
        when(authentication.getName()).thenReturn("user");
        when(userService.getUserByUsername("user")).thenReturn(currentUser);

        Listing listing = new Listing();
        listing.setProduct_id(1);
        listing.setProduct_quantity(10);
        when(listingRepository.findById(1)).thenReturn(Optional.of(listing));

        CartItem cartItem = new CartItem();
        cartItem.setQuantity(5);

        String viewName = listingController.addToCart(cartItem, 1, model);

        assertEquals("redirect:/listings", viewName);
        verify(shoppingCartService, times(1)).addListingToCart(listing, 5, 1);
    }

    @Test
    public void testShowAddListingForm() {
        String viewName = listingController.showAddListingForm(model);

        assertEquals("add-listing", viewName);
        assertTrue(model.containsAttribute("listing"));
    }

    @Test
    public void testSaveListing() {
        User currentUser = new User();
        currentUser.setId(1);
        when(authentication.getName()).thenReturn("user");
        when(userService.getUserByUsername("user")).thenReturn(currentUser);

        Listing listing = new Listing();
        listing.setProduct_quantity(10);
        listing.setProduct_price(100.0f);

        String viewName = listingController.saveListing(listing, model);

        assertEquals("redirect:/manage-listings", viewName);
        verify(listingService, times(1)).createListing(listing);
    }

    @Test
    public void testShowUpdateListingForm() {
        Listing listing = new Listing();
        when(listingService.getListingById(1)).thenReturn(Optional.of(listing));

        String viewName = listingController.showUpdateListingForm(1, model);

        assertEquals("update-listing", viewName);
        assertTrue(model.containsAttribute("listing"));
    }

    @Test
    public void testUpdateListing() {
        Listing listing = new Listing();
        listing.setProduct_id(1);
        when(listingService.getListingById(1)).thenReturn(Optional.of(listing));

        Listing updatedListing = new Listing();
        updatedListing.setProduct_name("Updated Name");
        updatedListing.setProduct_quantity(5);
        updatedListing.setProduct_description("Updated Description");
        updatedListing.setProduct_price(200.0f);

        String viewName = listingController.updateListing(updatedListing, 1, model);

        assertEquals("redirect:/listings", viewName);
        verify(listingService, times(1)).updateListing(1, listing);
    }

    @Test
    public void testManageListings() {
        User currentUser = new User();
        currentUser.setId(1);
        when(authentication.getName()).thenReturn("user");
        when(userService.getUserByUsername("user")).thenReturn(currentUser);

        List<Listing> userListings = new ArrayList<>();
        when(listingService.getListingsBySellerId(1)).thenReturn(userListings);

        String viewName = listingController.manageListings(model);

        assertEquals("manage-listings", viewName);
        assertTrue(model.containsAttribute("listings"));
    }

    @Test
    public void testDeleteListing() {
        String viewName = listingController.deleteListing(1);

        assertEquals("redirect:/manage-listings", viewName);
        verify(listingService, times(1)).deleteListing(1);
    }

    @Test
    public void testShowProductDetails() {
        Listing listing = new Listing();
        when(listingService.getListingById(1)).thenReturn(Optional.of(listing));

        String viewName = listingController.showProductDetails(1, model);

        assertEquals("product", viewName);
        assertTrue(model.containsAttribute("listing"));
    }

    @Test
    public void testShowCart() {
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn("user123");

        User user = new User();
        user.setId(1);
        user.setUsername("user123");
        user.setBalance(BigDecimal.valueOf(1000));
        when(userService.getUserByUsername("user123")).thenReturn(user);

        CartItem item1 = new CartItem();
        item1.setQuantity(2);
        item1.setListing(new Listing());
        item1.getListing().setProduct_price(100.0f);

        CartItem item2 = new CartItem();
        item2.setQuantity(1);
        item2.setListing(new Listing());
        item2.getListing().setProduct_price(200.0f);

        List<CartItem> cartItems = Arrays.asList(item1, item2);
        when(shoppingCartService.getCartItemsByBuyerId(1)).thenReturn(cartItems);

        when(paymentService.getLatestPaymentStatus(1)).thenReturn("APPROVED");

        String viewName = cartController.showCart(model, null);

        assertEquals("cart", viewName);
        assertEquals(cartItems, model.getAttribute("cartItems"));
        assertEquals(BigDecimal.valueOf(400.0), model.getAttribute("totalCost"));
        assertEquals(BigDecimal.valueOf(1000), model.getAttribute("balance"));
        assertEquals("APPROVED", model.getAttribute("paymentStatus"));
    }

    @Test
    void testFeatureListingGet() {
        when(listingService.getListingById(any(Integer.class))).thenReturn(Optional.of(new Listing()));

        String result = listingController.showFeatureListingForm(1, model);

        assertEquals("feature-listing", result);
        assertTrue(model.containsAttribute("listing"));
    }

    @Test
    void testFeatureListingGetFail() {
        when(listingService.getListingById(any(Integer.class))).thenReturn(Optional.empty());

        String result = listingController.showFeatureListingForm(1, model);

        assertEquals("redirect:/listings", result);
    }
    @Test
    void testFeatureListing() {
        Listing featuredListing = new Listing();
        featuredListing.setEnd_date(LocalDateTime.of(2024, 1, 1, 1, 1));

        when(listingService.getListingById(any(Integer.class))).thenReturn(Optional.of(new Listing()));

        when(listingService.updateListing(eq(1), any(Listing.class))).thenReturn(featuredListing);

        String result = listingController.featureListing(featuredListing, 1, model);

        assertEquals("redirect:/listings", result);
        verify(listingService).updateListing(eq(1), any(Listing.class));
    }

    @Test
    void testFeatureListingFail() {
        when(listingService.getListingById(any(Integer.class))).thenReturn(Optional.empty());

        String result = listingController.featureListing(new Listing(), 1, model);

        assertEquals("redirect:/listings", result);
        verify(listingService, never()).updateListing(any(Integer.class), any(Listing.class));
    }

    @Test
    void testDeleteFeatureListing() {
        Listing featuredListing = new Listing();
        featuredListing.setEnd_date(LocalDateTime.of(2024, 1, 1, 1, 1));

        when(listingService.getListingById(any(Integer.class))).thenReturn(Optional.of(new Listing()));

        when(listingService.updateListing(eq(1), any(Listing.class))).thenReturn(featuredListing);

        String result = listingController.deleteFeatureListing(new Listing(), 1, model);

        assertEquals("redirect:/listings", result);
        verify(listingService).updateListing(eq(1), any(Listing.class));
    }

    @Test
    void testDeleteFeatureListingFail() {
        when(listingService.getListingById(any(Integer.class))).thenReturn(Optional.empty());

        String result = listingController.deleteFeatureListing(new Listing(), 1, model);

        assertEquals("redirect:/listings", result);
        verify(listingService, never()).updateListing(any(Integer.class), any(Listing.class));
    }
    public void testAddToCartWithInvalidQuantity() {
        // Arrange
        User currentUser = new User();
        currentUser.setId(1);
        when(authentication.getName()).thenReturn("user");
        when(userService.getUserByUsername("user")).thenReturn(currentUser);

        Listing listing = new Listing();
        listing.setProduct_id(1);
        listing.setProduct_quantity(10);
        when(listingRepository.findById(1)).thenReturn(Optional.of(listing));

        CartItem cartItem = new CartItem();
        cartItem.setQuantity(0);

        // Act
        String viewName = listingController.addToCart(cartItem, 1, model);

        // Assert
        assertEquals("redirect:/listings", viewName);
        assertEquals("Quantity must be a positive number.", model.getAttribute("error"));
    }
    @Test
    public void testAddToCartWithInsufficientStock() {
        // Arrange
        User currentUser = new User();
        currentUser.setId(1);
        when(authentication.getName()).thenReturn("user");
        when(userService.getUserByUsername("user")).thenReturn(currentUser);

        Listing listing = new Listing();
        listing.setProduct_id(1);
        listing.setProduct_quantity(5);
        when(listingRepository.findById(1)).thenReturn(Optional.of(listing));

        CartItem cartItem = new CartItem();
        cartItem.setQuantity(10);

        // Act
        String viewName = listingController.addToCart(cartItem, 1, model);

        // Assert
        assertEquals("redirect:/listings", viewName);
        assertEquals("Not enough stock available.", model.getAttribute("error"));
    }

    @Test
    public void testAddToCartWithListingNotFound() {
        // Arrange
        User currentUser = new User();
        currentUser.setId(1);
        when(authentication.getName()).thenReturn("user");
        when(userService.getUserByUsername("user")).thenReturn(currentUser);

        CartItem cartItem = new CartItem();
        cartItem.setQuantity(5);

        // Act
        String viewName = listingController.addToCart(cartItem, 1, model);

        // Assert
        assertEquals("redirect:/listings", viewName);
    }
    @Test
    public void testUpdateListingWithListingNotFound() {
        // Arrange
        Listing updatedListing = new Listing();
        updatedListing.setProduct_name("Updated Name");
        updatedListing.setProduct_quantity(5);
        updatedListing.setProduct_description("Updated Description");
        updatedListing.setProduct_price(200.0f);

        // Act
        String viewName = listingController.updateListing(updatedListing, 1, model);

        // Assert
        assertEquals("redirect:/update-listings", viewName);
    }

    @Test
    public void testGetAllListingsWithNullSellerId() {
        // Arrange
        User currentUser = new User();
        currentUser.setId(1);
        when(authentication.getName()).thenReturn("user");
        when(userService.getUserByUsername("user")).thenReturn(currentUser);

        Listing listing1 = new Listing();
        listing1.setVisible(true);
        listing1.setSellerId(2);

        Listing listing2 = new Listing();
        listing2.setVisible(true);
        listing2.setSellerId(null);

        List<Listing> listings = new ArrayList<>();
        listings.add(listing1);
        listings.add(listing2);

        when(listingService.getAllListings()).thenReturn(listings);

        // Act
        String viewName = listingController.getAllListings(model);

        // Assert
        assertEquals("listings", viewName);
        List<Listing> visibleListings = (List<Listing>) model.getAttribute("listings");
        assertNotNull(visibleListings);
        assertEquals(1, visibleListings.size());  // Only listing1 should be included

    }
    @Test
    public void testSaveListingWithNegativeStockOrPrice() {
        // Arrange
        Listing listing = new Listing();
        listing.setProduct_quantity(-1); // Set invalid stock
        listing.setProduct_price(100.0f);

        // Act
        String viewName = listingController.saveListing(listing, model);

        // Assert
        assertEquals("add-listing", viewName);
        assertTrue(model.containsAttribute("error"));
        assertEquals("Stock and price cannot be negative.", model.getAttribute("error"));

        // Test for negative price
        listing.setProduct_quantity(10);
        listing.setProduct_price(-100.0f); // Set invalid price

        viewName = listingController.saveListing(listing, model);

        assertEquals("add-listing", viewName);
        assertTrue(model.containsAttribute("error"));
        assertEquals("Stock and price cannot be negative.", model.getAttribute("error"));
    }

    @Test
    public void testAddToCartListingNotFound() {
        // Arrange
        when(listingRepository.findById(1)).thenReturn(Optional.empty());

        CartItem cartItem = new CartItem();
        cartItem.setQuantity(5);
        // Act
        String viewName = listingController.addToCart(cartItem, 1, model);

        // Assert
        assertEquals("redirect:/listings", viewName);
        verify(shoppingCartService, never()).addListingToCart(any(), anyInt(), anyInt());
    }

    @Test
    public void testAddToCartInvalidQuantity() {
        // Arrange
        Listing listing = new Listing();
        listing.setProduct_id(1);
        listing.setProduct_quantity(10);
        when(listingRepository.findById(1)).thenReturn(Optional.of(listing));

        CartItem cartItem = new CartItem();
        cartItem.setQuantity(0); // Invalid quantity

        // Act
        String viewName = listingController.addToCart(cartItem, 1, model);

        // Assert
        assertEquals("redirect:/listings", viewName);
        assertTrue(model.containsAttribute("error"));
        assertEquals("Quantity must be a positive number.", model.getAttribute("error"));
        verify(shoppingCartService, never()).addListingToCart(any(), anyInt(), anyInt());
    }

    @Test
    public void testAddToCartInsufficientStock() {
        // Arrange
        User currentUser = new User();
        currentUser.setId(1);
        when(authentication.getName()).thenReturn("user");
        when(userService.getUserByUsername("user")).thenReturn(currentUser);

        Listing listing = new Listing();
        listing.setProduct_id(1);
        listing.setProduct_quantity(5); // Set stock less than requested quantity
        when(listingRepository.findById(1)).thenReturn(Optional.of(listing));

        CartItem cartItem = new CartItem();
        cartItem.setQuantity(10); // Requested quantity more than stock

        // Act
        String viewName = listingController.addToCart(cartItem, 1, model);

        // Assert
        assertEquals("redirect:/listings", viewName);
        assertTrue(model.containsAttribute("error"));
        assertEquals("Not enough stock available.", model.getAttribute("error"));
        verify(shoppingCartService, never()).addListingToCart(any(), anyInt(), anyInt());
    }

    @Test
    public void testUpdateListingNotFound() {
        // Arrange
        when(listingService.getListingById(1)).thenReturn(Optional.empty());

        Listing updatedListing = new Listing();
        updatedListing.setProduct_name("Updated Name");
        updatedListing.setProduct_quantity(5);
        updatedListing.setProduct_description("Updated Description");
        updatedListing.setProduct_price(200.0f);

        // Act
        String viewName = listingController.updateListing(updatedListing, 1, model);

        // Assert
        assertEquals("redirect:/update-listings", viewName);
        verify(listingService, never()).updateListing(any(Integer.class), any());
    }

    @Test
    public void testShowCartEmpty() {
        // Arrange
        User currentUser = new User();
        currentUser.setId(1);
        when(authentication.getName()).thenReturn("user");
        when(userService.getUserByUsername("user")).thenReturn(currentUser);

        when(shoppingCartService.getCartItemsByBuyerId(1)).thenReturn(new ArrayList<>());

        // Act
        String viewName = listingController.showCart();

        // Assert
        assertEquals("cart", viewName);
    }

}
