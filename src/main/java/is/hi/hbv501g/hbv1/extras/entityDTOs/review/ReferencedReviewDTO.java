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

public class ReferencedReviewDTO {
    private Long id;
    private int rating;
    private String text;
    private String title;

    private ReferencedUserDTO user;
    private ReferencedGameDTO game;

    public ReferencedReviewDTO(Review review) {
        this.id = review.getId();
        this.rating = review.getRating();
        this.text = review.getText();
        this.title = review.getTitle();

        this.user = new ReferencedUserDTO(review.getUser());
        this.game = new ReferencedGameDTO(review.getGame());
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

    public ReferencedUserDTO getUser() {
        return user;
    }

    public ReferencedGameDTO getGame() {
        return game;
    }
}
