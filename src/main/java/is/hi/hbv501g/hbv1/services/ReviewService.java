package is.hi.hbv501g.hbv1.services;

import is.hi.hbv501g.hbv1.extras.DTOs.ReviewToCreate;
import is.hi.hbv501g.hbv1.extras.DTOs.ReviewToUpdate;
import is.hi.hbv501g.hbv1.persistence.entities.Game;
import is.hi.hbv501g.hbv1.persistence.entities.Review;
import is.hi.hbv501g.hbv1.persistence.entities.User;

public interface ReviewService {
    
    Review postReview(User user, Game game, ReviewToCreate incomingReviewDTO) throws IllegalArgumentException;
    
    // fixed for implementation and use of ReviewToUpdate DTO
    Review updateReview(User user, Long gameID, Long reviewID, ReviewToUpdate updateReviewDTO)
            throws SecurityException, IllegalArgumentException;
    void deleteReview(User user, Long gameID, Long reviewID)
            throws SecurityException, IllegalArgumentException;
}