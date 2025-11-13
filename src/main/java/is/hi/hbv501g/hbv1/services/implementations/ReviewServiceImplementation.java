package is.hi.hbv501g.hbv1.services.implementations;

import is.hi.hbv501g.hbv1.extras.DTOs.ReviewToCreate; 
import is.hi.hbv501g.hbv1.extras.DTOs.ReviewToUpdate; 
import is.hi.hbv501g.hbv1.persistence.entities.Game;
import is.hi.hbv501g.hbv1.persistence.entities.Review;
import is.hi.hbv501g.hbv1.persistence.entities.User;
import is.hi.hbv501g.hbv1.persistence.repositories.ReviewRepository;
import is.hi.hbv501g.hbv1.services.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class ReviewServiceImplementation implements ReviewService {

    private final ReviewRepository reviewRepository;

    @Autowired
    public ReviewServiceImplementation(ReviewRepository reviewRepository) {
        this.reviewRepository = reviewRepository;
    }

    @Override
    // changed to allow ReviewToCreate DTO
    public Review postReview(User user, Game game, ReviewToCreate incomingReviewDTO) throws IllegalArgumentException {
        Optional<Review> existing = reviewRepository.findByGameAndUser(game, user);
        
        if (existing.isPresent()) {
            throw new IllegalArgumentException("You have already submitted a review for this game");
        }

        // now creating the Review entity from the DTO data
        Review newReview = new Review(); 
        newReview.setTitle(incomingReviewDTO.getTitle());
        newReview.setText(incomingReviewDTO.getText());
        newReview.setRating(incomingReviewDTO.getRating());

        //the relationships
        newReview.setUser(user);
        newReview.setGame(game);

        Review savedReview = reviewRepository.save(newReview);

        return savedReview;
    }
    
    @Override
    //changed to accept ReviewToUpdate DTO
    public Review updateReview(User user, Long gameID, Long reviewID, ReviewToUpdate updateReviewDTO)
            throws SecurityException, IllegalArgumentException {
        
        //find the review 
        Review existingReview = reviewRepository.findById(reviewID)
                .orElseThrow(() -> new IllegalArgumentException("Review not found with ID: " + reviewID));

        // check if the review belongs to the game
        if (!Long.valueOf(existingReview.getGame().getId()).equals(gameID)) { 
            throw new IllegalArgumentException("This review does not belong to the specified game");
        }

        //check permission
        if (!existingReview.getUser().getId().equals(user.getId())) { 
            throw new SecurityException("Access Denied: You are not the author of this review");
        }

        //updates fields that are provided or those that are not null
        if (updateReviewDTO.getTitle() != null) {
            existingReview.setTitle(updateReviewDTO.getTitle());
        }
        if (updateReviewDTO.getText() != null) {
            existingReview.setText(updateReviewDTO.getText());
        }
        // Rating is an Int in the DTO, so we check for null
        if (updateReviewDTO.getRating() != null) {
            existingReview.setRating(updateReviewDTO.getRating());
        }

        return reviewRepository.save(existingReview);
    }
    
    @Override
    public void deleteReview(User user, Long gameID, Long reviewID)
            throws SecurityException, IllegalArgumentException {

        Review reviewToDelete = reviewRepository.findById(reviewID)
                .orElseThrow(() -> new IllegalArgumentException("Review not found with ID: " + reviewID));


        if (!Long.valueOf(reviewToDelete.getGame().getId()).equals(gameID)) { 
            throw new IllegalArgumentException("This review does not belong to the specified game");
        }


        if (!reviewToDelete.getUser().getId().equals(user.getId())) { 
            throw new SecurityException("Access Denied: You are not the author of this review");
        }
        reviewRepository.delete(reviewToDelete);
    }
}