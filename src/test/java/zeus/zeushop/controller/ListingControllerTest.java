package zeus.zeushop.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;
import zeus.zeushop.service.PaymentService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Captor;
import org.mockito.ArgumentCaptor;
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
import zeus.zeushop.repository.CartItemRepository;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.Map;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;


public class ListingControllerTest {

    @Mock
    private UserService userService;

    @Captor
    private ArgumentCaptor<String> keyCaptor;

    @Captor
    private ArgumentCaptor<Object> valueCaptor;

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

    @Mock
    private CartItemRepository cartItemRepository;

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
        listing2.setSellerId(3);

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
        assertEquals(2, visibleListings.size());
    }

    @Test
    public void testAddToCart() {
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
        cartItem.setQuantity(5);

        // Act
        String viewName = listingController.addToCart(cartItem, 1, model);

        // Assert
        assertEquals("redirect:/listings", viewName);
        verify(shoppingCartService, times(1)).addListingToCart(listing, 5, 1);
    }

    @Test
    public void testShowAddListingForm() {
        // Act
        String viewName = listingController.showAddListingForm(model);

        // Assert
        assertEquals("add-listing", viewName);
        assertTrue(model.containsAttribute("listing"));
    }

    @Test
    public void testSaveListing() {
        // Arrange
        User currentUser = new User();
        currentUser.setId(1);
        when(authentication.getName()).thenReturn("user");
        when(userService.getUserByUsername("user")).thenReturn(currentUser);

        Listing listing = new Listing();
        listing.setProduct_quantity(10);
        listing.setProduct_price(100.0f);

        // Act
        String viewName = listingController.saveListing(listing, model);

        // Assert
        assertEquals("redirect:/manage-listings", viewName);
        verify(listingService, times(1)).createListing(listing);
    }

    @Test
    public void testShowUpdateListingForm() {
        // Arrange
        Listing listing = new Listing();
        when(listingService.getListingById(1L)).thenReturn(Optional.of(listing));

        // Act
        String viewName = listingController.showUpdateListingForm(1, model);

        // Assert
        assertEquals("update-listing", viewName);
        assertTrue(model.containsAttribute("listing"));
    }

    @Test
    public void testUpdateListing() {
        // Arrange
        Listing listing = new Listing();
        listing.setProduct_id(1);
        when(listingService.getListingById(1L)).thenReturn(Optional.of(listing));

        Listing updatedListing = new Listing();
        updatedListing.setProduct_name("Updated Name");
        updatedListing.setProduct_quantity(5);
        updatedListing.setProduct_description("Updated Description");
        updatedListing.setProduct_price(200.0f);

        // Act
        String viewName = listingController.updateListing(updatedListing, 1L, model);

        // Assert
        assertEquals("redirect:/listings", viewName);
        verify(listingService, times(1)).updateListing(1L, listing);
    }

    @Test
    public void testManageListings() {
        // Arrange
        User currentUser = new User();
        currentUser.setId(1);
        when(authentication.getName()).thenReturn("user");
        when(userService.getUserByUsername("user")).thenReturn(currentUser);

        List<Listing> userListings = new ArrayList<>();
        when(listingService.getListingsBySellerId(1)).thenReturn(userListings);

        // Act
        String viewName = listingController.manageListings(model);

        // Assert
        assertEquals("manage-listings", viewName);
        assertTrue(model.containsAttribute("listings"));
    }

    @Test
    public void testDeleteListing() {
        // Act
        String viewName = listingController.deleteListing(1L);

        // Assert
        assertEquals("redirect:/manage-listings", viewName);
        verify(listingService, times(1)).deleteListing(1L);
    }

    @Test
    public void testShowProductDetails() {
        // Arrange
        Listing listing = new Listing();
        when(listingService.getListingById(1L)).thenReturn(Optional.of(listing));

        // Act
        String viewName = listingController.showProductDetails(1, model);

        // Assert
        assertEquals("product", viewName);
        assertTrue(model.containsAttribute("listing"));
    }

    @Test
    public void testShowCart() {
        // Mocking the authenticated user
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn("user123");

        // Mocking user service
        User user = new User();
        user.setId(1);
        user.setUsername("user123");
        user.setBalance(BigDecimal.valueOf(1000));
        when(userService.getUserByUsername("user123")).thenReturn(user);

        // Mocking cart items
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

        // Mocking payment service
        when(paymentService.getLatestPaymentStatus(1)).thenReturn("APPROVED");

        // Call the method under test
        String viewName = cartController.showCart(model, null);

        // Assert the model attributes
        assertEquals("cart", viewName);
        assertEquals(cartItems, model.getAttribute("cartItems"));
        assertEquals(BigDecimal.valueOf(400.0), model.getAttribute("totalCost"));
        assertEquals(BigDecimal.valueOf(1000), model.getAttribute("balance"));
        assertEquals("APPROVED", model.getAttribute("paymentStatus"));
    }

    @Test
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
        String viewName = listingController.updateListing(updatedListing, 1L, model);

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



}
