package zeus.zeushop.controller;
import zeus.zeushop.model.Listing;
import zeus.zeushop.service.ListingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/listings")
public class ListingController {
    @Autowired
    private ListingService listingService;

    @PostMapping
    public Listing createListing(@RequestBody Listing listing) {
        return listingService.createListing(listing);
    }

    @GetMapping
    public List<Listing> getAllListings() {
        return listingService.getAllListings();
    }

    @GetMapping("/{id}")
    public Optional<Listing> getListingById(@PathVariable String id) {
        return listingService.getListingById(id);
    }

    @PutMapping("/{id}")
    public Listing updateListing(@PathVariable String id, @RequestBody Listing listingDetails) {
        return listingService.updateListing(id, listingDetails);
    }

    @DeleteMapping("/{id}")
    public void deleteListing(@PathVariable String id) {
        listingService.deleteListing(id);
    }
}