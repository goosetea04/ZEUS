package zeus.zeushop.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import zeus.zeushop.model.TopUp;
import zeus.zeushop.service.StaffBoardService;

import java.util.List;

@RestController
@RequestMapping("/api/staff")
public class StaffBoardController {

    private final StaffBoardService staffBoardService;

    @Autowired
    public StaffBoardController(StaffBoardService staffBoardService) {
        this.staffBoardService = staffBoardService;
    }

    @PostMapping("/approve/{topUpId}")
    public ResponseEntity<?> approveTopUp(@PathVariable String topUpId) {
        boolean success = staffBoardService.approveTopUp(topUpId);
        if (success) {
            return ResponseEntity.ok("Top-up approved successfully.");
        } else {
            return ResponseEntity.badRequest().body("Failed to approve top-up. It may not exist or is not pending.");
        }
    }

    @GetMapping("/top-ups/status/{status}")
    public ResponseEntity<List<TopUp>> getTopUpsByStatus(@PathVariable String status) {
        List<TopUp> topUps = staffBoardService.getTopUpsByStatus(status);
        return ResponseEntity.ok(topUps);
    }

    @GetMapping("/top-ups")
    public ResponseEntity<List<TopUp>> getAllTopUps() {
        List<TopUp> topUps = staffBoardService.getAllTopUps();
        return ResponseEntity.ok(topUps);
    }

    @GetMapping("/user/{userId}/top-ups")
    public ResponseEntity<List<TopUp>> getUserTopUps(@PathVariable String userId) {
        List<TopUp> topUps = staffBoardService.getUserTopUps(userId);
        return ResponseEntity.ok(topUps);
    }
}
