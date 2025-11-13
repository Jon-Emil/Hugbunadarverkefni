package is.hi.hbv501g.hbv1.extras.entityDTOs.review;

import com.fasterxml.jackson.annotation.JsonIgnore;
import is.hi.hbv501g.hbv1.extras.entityDTOs.game.ReferencedGameDTO;
import is.hi.hbv501g.hbv1.extras.entityDTOs.user.ReferencedUserDTO;
import is.hi.hbv501g.hbv1.persistence.entities.Game;
import is.hi.hbv501g.hbv1.persistence.entities.Review;
import is.hi.hbv501g.hbv1.persistence.entities.User;
import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.*;

/**
 * this is a DTO that is used as the second layer when we provide a user with information about a review
 * so it includes all info about a review excluding references to other objects to avoid infinite recursion
 */
public class ReferencedReviewDTO {
    private Long id;
    private int rating;
    private String text;
    private String title;
    private String author;
    private String gameTitle;

    public ReferencedReviewDTO(Review review) {
        this.id = review.getId();
        this.rating = review.getRating();
        this.text = review.getText();
        this.title = review.getTitle();
        this.author = review.getUser().getUsername();
        this.gameTitle = review.getGame().getTitle();
    }

    public Long getId() {
        return id;
    }

    public int getRating() {
        return rating;
    }

    public String getText() {
        return text;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getGameTitle() {
        return gameTitle;
    }
}
