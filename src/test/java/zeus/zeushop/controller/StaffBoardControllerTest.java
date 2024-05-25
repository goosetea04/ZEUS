package zeus.zeushop.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.ui.Model;
import zeus.zeushop.model.TopUp;
import zeus.zeushop.service.StaffBoardService;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class StaffBoardControllerTest {

    @Mock
    private StaffBoardService staffBoardService;

    @Mock
    private Model model;

    private StaffBoardController staffBoardController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        staffBoardController = new StaffBoardController(staffBoardService);
    }

    @Test
    void testGetTopUpsByStatus() {
        // Given
        List<TopUp> topUps = Arrays.asList(new TopUp(), new TopUp()); // Sample top-ups
        String status = "PENDING";

        when(staffBoardService.getTopUpsByStatus(status)).thenReturn(topUps);

        // When
        String viewName = staffBoardController.getTopUpsByStatus(status, model);

        // Then
        assertEquals("staffdashboard", viewName);
        verify(model).addAttribute("topUps", topUps);
        verify(model).addAttribute("status", status);
    }

    // Add more test methods for other controller actions...
}
