package zeus.zeushop.controller;
import zeus.zeushop.model.*;
import zeus.zeushop.service.*;
import zeus.zeushop.repository.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import zeus.zeushop.model.Listing;
import zeus.zeushop.model.User;
import zeus.zeushop.service.ListingService;
import zeus.zeushop.service.UserService;

public class ListingControllerTest {

    private MockMvc mockMvc;

    @Mock
    private ListingService listingService;

    @Mock
    private UserService userService;

    @InjectMocks
    private ListingController listingController;

    @Mock
    private Authentication authentication;

    @Mock
    private SecurityContext securityContext;
    @Mock
    private ListingRepository listingRepository;
    @Mock
    private CartItemRepository cartItemRepository;
    @Mock
    private ShoppingCartService shoppingCartService;
    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        listingController = new ListingController(listingService, shoppingCartService, listingRepository, cartItemRepository, userService);
        mockMvc = MockMvcBuilders.standaloneSetup(listingController).build();

        // Set up mock authentication and security context
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        when(authentication.getName()).thenReturn("testuser");

        User currentUser = new User();
        currentUser.setId(1);
        currentUser.setUsername("testuser");
        currentUser.setPassword("password");
        currentUser.setRole("USER");
        currentUser.setBalance(BigDecimal.ZERO);
        when(userService.getUserByUsername("testuser")).thenReturn(currentUser);
    }

    @Test
    public void testGetAllListings() throws Exception {
        // Arrange
        List<Listing> visibleListings = Arrays.asList(
                new Listing(1L, "Product 1", 10, "Description 1", 19.99, 1L, true),
                new Listing(2L, "Product 2", 5, "Description 2", 29.99, 2L, true)
        );
        when(listingService.getAllListings()).thenReturn(visibleListings);
        // Arrange
        User currentUser = new User();
        currentUser.setId(1);
        currentUser.setUsername("testuser");
        currentUser.setPassword("password");
        currentUser.setRole("USER");
        currentUser.setBalance(BigDecimal.ZERO);
        when(userService.getUserByUsername("testuser")).thenReturn(currentUser);

        // Act and Assert
        mockMvc.perform(get("/listings"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("listings", visibleListings));
    }


    // Add more test cases for other controller methods...

    @Test
    public void testAddToCart_ValidInput() throws Exception {
        // Arrange
        Listing listing = new Listing(1L, "Product 1", 10, "Description 1", 19.99, 1L, true);
        when(listingRepository.findById(1)).thenReturn(Optional.of(listing));
        User currentUser = new User();
        currentUser.setId(2);
        currentUser.setUsername("testuser");
        currentUser.setPassword("password");
        currentUser.setRole("USER");
        currentUser.setBalance(BigDecimal.ZERO);
        when(userService.getUserByUsername("testuser")).thenReturn(currentUser);

        // Act and Assert
        mockMvc.perform(post("/add-to-cart")
                        .param("listingId", "1")
                        .param("quantity", "2"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/listings"));

        verify(shoppingCartService).addListingToCart(listing, 2, currentUser.getId());
    }

    @Test
    public void testAddToCart_InvalidListingId() throws Exception {
        // Arrange
        when(listingRepository.findById(1)).thenReturn(Optional.empty());

        // Act and Assert
        mockMvc.perform(post("/add-to-cart")
                        .param("listingId", "1")
                        .param("quantity", "2"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/listings"));

        verifyNoInteractions(shoppingCartService);
    }

    @Test
    public void testAddToCart_NegativeQuantity() throws Exception {
        // Arrange
        Listing listing = new Listing(1L, "Product 1", 10, "Description 1", 19.99, 1L, true);
        when(listingRepository.findById(1)).thenReturn(Optional.of(listing));

        // Act and Assert
        mockMvc.perform(post("/add-to-cart")
                        .param("listingId", "1")
                        .param("quantity", "-2"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/listings"))
                .andExpect(view().name("redirect:/listings"));

        verifyNoInteractions(shoppingCartService);
    }

    // Add more test cases for other scenarios and controller methods...
}