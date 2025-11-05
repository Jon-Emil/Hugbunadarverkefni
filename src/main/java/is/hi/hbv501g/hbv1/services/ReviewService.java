package is.hi.hbv501g.hbv1.services;

import is.hi.hbv501g.hbv1.persistence.entities.Game;
import is.hi.hbv501g.hbv1.persistence.entities.Review;
import is.hi.hbv501g.hbv1.persistence.entities.User;


public interface ReviewService {

    /**
     * Creates and saves a new review.
     * Checks if the user has already reviewed this game.
     * @param user The user posting the review.
     * @param game The game being reviewed.
     * @param incomingReview A Review object containing the title, text, and rating.
     * @throws IllegalArgumentException if the user has already reviewed this game.
     */
    Review postReview(User user, Game game, Review incomingReview) throws IllegalArgumentException;

    /**
     * change review/update the review
     * @param user The user attempting the update.
     * @param gameID The ID of the game the review belongs to.
     * @param reviewID The ID of the review to update.
     * @param updateReviewData A Review object with the new data.
     * @throws SecurityException if the user is not the original author.
     * @throws IllegalArgumentException if the review is not found or doesn't belong to the game.
     */
    void updateReview(User user, Long gameID, Long reviewID, Review updateReviewData)
            throws SecurityException, IllegalArgumentException;

    /**
     * Deletes a review.
     * @param user The user attempting the delete.
     * @param gameID The ID of the game the review belongs to.
     * @param reviewID The ID of the review to delete.
     * @throws SecurityException if the user is not the original author.
     * @throws IllegalArgumentException if the review is not found or doesn't belong to the game.
     */
    void deleteReview(User user, Long gameID, Long reviewID)
            throws SecurityException, IllegalArgumentException;
}