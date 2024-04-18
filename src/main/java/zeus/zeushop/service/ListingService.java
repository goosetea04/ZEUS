package zeus.zeushop.service;
import zeus.zeushop.model.Listing;
import java.util.List;
import java.util.Optional;

public interface ListingService {
    public Listing createListing(Listing listing);
    public List<Listing> getAllListings();
    public Optional<Listing> getListingById(String id);
    public Listing updateListing(String id, Listing listingDetails);
    public void deleteListing(String id);
}