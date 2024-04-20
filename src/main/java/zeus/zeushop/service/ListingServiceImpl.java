package zeus.zeushop.service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import zeus.zeushop.model.Listing;
import zeus.zeushop.repository.ListingRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ListingServiceImpl implements ListingService {
    @Autowired
    private ListingRepository listingRepository;

    public Listing createListing(Listing listing) {
        return listingRepository.save(listing);
    }

    public List<Listing> getAllListings() {
        return listingRepository.findAll();
    }

    public Optional<Listing> getListingById(Long id) {
        return listingRepository.findById(id);
    }

    public Listing updateListing(Long id, Listing listingDetails) {
        Optional<Listing> listing = listingRepository.findById(id);
        if (listing.isPresent()) {
            Listing existingListing = listing.get();
            existingListing.setProduct_name(listingDetails.getProduct_name());
            existingListing.setProduct_quantity(listingDetails.getProduct_quantity());
            existingListing.setProduct_price(listingDetails.getProduct_price());
            existingListing.setEndDate(listingDetails.getEndDate());
            return listingRepository.save(existingListing);
        }
        return null;
    }

    public void deleteListing(Long id) { listingRepository.deleteById(id); }

    @Override
    public List<Listing> getAllFeatured() {
        return listingRepository.findByEndDateGreaterThanEqual(LocalDateTime.now());
    }
}