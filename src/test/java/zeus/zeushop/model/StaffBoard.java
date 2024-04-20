package zeus.zeushop.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class StaffDashboardTest {
    private StaffBoard dashboard;
    private List<TopUp> topUps;

    @BeforeEach
    void setUp() {
        topUps = new ArrayList<>(Arrays.asList(
                new TopUp("1", "user1", 100.0, "PENDING", LocalDateTime.now(), LocalDateTime.now()),
                new TopUp("2", "user2", 200.0, "APPROVED", LocalDateTime.now(), LocalDateTime.now()),
                new TopUp("3", "user3", 300.0, "CANCELLED", LocalDateTime.now(), LocalDateTime.now())
        ));
        dashboard = new StaffBoard();
        dashboard.setStaffId("staff1");
        dashboard.setTopUps(topUps);
    }

    @Test
    void testApproveTopUp() {
        // Assumption: topUps.get(0) has ID "1" and is initially "PENDING"
        assertEquals("PENDING", topUps.getFirst().getStatus(), "Initial status should be PENDING");

        // Approve an existing "PENDING" top-up
        dashboard.approveTopUp("1");
        assertEquals("APPROVED", topUps.get(0).getStatus(), "Status should be APPROVED after approval");

        // Ensure the status of an already approved top-up remains unchanged
        assertEquals("APPROVED", topUps.get(1).getStatus(), "Status of already approved should remain APPROVED");

    }

    @Test
    void testFilterTopUpsByStatus() {
        // Filter by "PENDING"
        List<TopUp> pendingTopUps = dashboard.filterTopUpsByStatus("PENDING");
        assertEquals(1, pendingTopUps.size());
        assertEquals("1", pendingTopUps.getFirst().getTopUpId());

        // Filter by "APPROVED"
        List<TopUp> approvedTopUps = dashboard.filterTopUpsByStatus("APPROVED");
        assertEquals(1, approvedTopUps.size());
        assertEquals("2", approvedTopUps.getFirst().getTopUpId());

        // Filter by a status with no matching records
        List<TopUp> cancelledTopUps = dashboard.filterTopUpsByStatus("CANCELLED");
        assertEquals(1, cancelledTopUps.size());
        assertEquals("3", cancelledTopUps.getFirst().getTopUpId());
    }
}
