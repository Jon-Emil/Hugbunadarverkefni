package is.hi.hbv501g.hbv1.services.implementations;

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
    public Review postReview(User user, Game game, Review incomingReview) throws IllegalArgumentException {
        // check if a review exists
        Optional<Review> existing = reviewRepository.findByGameAndUser(game, user);
        
        if (existing.isPresent()) {
            throw new IllegalArgumentException("You have already submitted a review for this game");
        }

        // ncomingReview only has text, title, rating.
        // and set the user and game relationships.
        incomingReview.setUser(user);
        incomingReview.setGame(game);

        Review savedReview = reviewRepository.save(incomingReview);

        return savedReview;
    }
    
    @Override
    public void updateReview(User user, Long gameID, Long reviewID, Review updateReviewData)
            throws SecurityException, IllegalArgumentException {
        
        //find the review in the database
        Review existingReview = reviewRepository.findById(reviewID)
                .orElseThrow(() -> new IllegalArgumentException("Review not found with ID: " + reviewID));

        // check if the review belongs to the game
        if (existingReview.getGame().getId() != gameID) {
            throw new IllegalArgumentException("This review does not belong to the specified game");
        }

        //check perm
        if (existingReview.getUser().getId() != user.getId()) { 
            throw new SecurityException("Access Denied: You are not the author of this review");
        }

        existingReview.setTitle(updateReviewData.getTitle());
        existingReview.setText(updateReviewData.getText());
        existingReview.setRating(updateReviewData.getRating());

        reviewRepository.save(existingReview);
    }
    
    @Override
    public void deleteReview(User user, Long gameID, Long reviewID)
            throws SecurityException, IllegalArgumentException {

        Review reviewToDelete = reviewRepository.findById(reviewID)
                .orElseThrow(() -> new IllegalArgumentException("Review not found with ID: " + reviewID));

        
        if (reviewToDelete.getGame().getId() != gameID) { // Using !=
            throw new IllegalArgumentException("This review does not belong to the specified game");
        }

        if (reviewToDelete.getUser().getId() != user.getId()) { // Using !=
            throw new SecurityException("Access Denied: You are not the author of this review");
        }
        reviewRepository.delete(reviewToDelete);
    }
}