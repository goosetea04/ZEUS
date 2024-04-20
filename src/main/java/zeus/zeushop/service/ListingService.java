package zeus.zeushop.service;
import zeus.zeushop.model.Listing;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ListingService {
    public Listing createListing(Listing listing);
    public List<Listing> getAllListings();
    public Optional<Listing> getListingById(Long id);
    public Listing updateListing(Long id, Listing listingDetails);
    public void deleteListing(Long id);

    public List<Listing> getAllFeatured();
}