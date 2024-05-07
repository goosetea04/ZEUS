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

    @Override
    public void deleteListing(Long id) {
        listingRepository.deleteById(id.intValue());
    }

    @Override
    public Optional<Listing> getListingById(Long id) {
        return listingRepository.findById(id.intValue());
    }

    @Override
    public Listing updateListing(Long id, Listing listingDetails) {
        Optional<Listing> optionalListing = listingRepository.findById(id.intValue());
        if (optionalListing.isPresent()) {
            Listing existingListing = optionalListing.get();
            existingListing.setProduct_name(listingDetails.getProduct_name());
            existingListing.setProduct_quantity(listingDetails.getProduct_quantity());
            existingListing.setProduct_price(listingDetails.getProduct_price());
            existingListing.setProduct_description(listingDetails.getProduct_description());
            existingListing.setEndDate(listingDetails.getEndDate());
            return listingRepository.save(existingListing);
        } else {
            // Handle the case where the listing with the given id is not found
            return null;
        }
    }
    @Override
    public List<Listing> getAllFeatured() {
        return listingRepository.findByEndDateGreaterThanEqual(LocalDateTime.now());
    }

    @Override
    public List<Listing> getListingsBySellerId(Integer sellerId) {
        return listingRepository.findBySellerId(sellerId);
    }

}