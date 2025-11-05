package is.hi.hbv501g.hbv1.services;

import is.hi.hbv501g.hbv1.extras.DTOs.ReviewToCreate;
import is.hi.hbv501g.hbv1.extras.DTOs.ReviewToUpdate;
import is.hi.hbv501g.hbv1.persistence.entities.Game;
import is.hi.hbv501g.hbv1.persistence.entities.Review;
import is.hi.hbv501g.hbv1.persistence.entities.User;

public interface ReviewService {
    
    /**
     * Creating a new review for a game.
     * @param user The user submitting the review.
     * @param game The game being reviewed.
     * @param incomingReviewDTO The data for the new review.
     * @return The newly created Review entity.
     * @throws IllegalArgumentException If the user has already reviewed the game.
     */
    Review postReview(User user, Game game, ReviewToCreate incomingReviewDTO) throws IllegalArgumentException;
    
    /**
     * updates an existing review.
     * @param user The authenticated user attempting the update.
     * @param gameID The ID of the game the review belongs to.
     * @param reviewID The ID of the review to update.
     * @param updateReviewDTO The partial data for the update (PATCH logic).
     * @throws SecurityException If the user is not the author of the review.
     * @throws IllegalArgumentException If the review or game is not found.
     */
    // fixed for implementation and use of ReviewToUpdate DTO
    void updateReview(User user, Long gameID, Long reviewID, ReviewToUpdate updateReviewDTO)
            throws SecurityException, IllegalArgumentException;

    /**
     * Delete a review.
     * @param user The authenticated user attempting the deletion.
     * @param gameID The ID of the game the review belongs to.
     * @param reviewID The ID of the review to delete.
     * @throws SecurityException If the user is not the author of the review.
     * @throws IllegalArgumentException If the review or game is not found.
     */
    void deleteReview(User user, Long gameID, Long reviewID)
            throws SecurityException, IllegalArgumentException;
}