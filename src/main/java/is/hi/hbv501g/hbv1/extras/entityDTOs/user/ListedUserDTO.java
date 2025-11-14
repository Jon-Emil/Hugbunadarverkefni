package is.hi.hbv501g.hbv1.extras.entityDTOs.user;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import is.hi.hbv501g.hbv1.extras.entityDTOs.game.ReferencedGameDTO;
import is.hi.hbv501g.hbv1.extras.entityDTOs.review.ReferencedReviewDTO;
import is.hi.hbv501g.hbv1.persistence.entities.Game;
import is.hi.hbv501g.hbv1.persistence.entities.Review;
import is.hi.hbv501g.hbv1.persistence.entities.Role;
import is.hi.hbv501g.hbv1.persistence.entities.User;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

public class ListedUserDTO {
    private Long id;
    private String username;
    private String profilePictureURL;
    private String description;

    private int favoriteAmount;
    private int wantsToPlayAmount;
    private int hasPlayedAmount;
    private int followsAmount;
    private int followedByAmount;
    private int reviewAmount;

    public ListedUserDTO(User user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.profilePictureURL = user.getProfilePictureURL();
        this.description = user.getDescription();

        this.favoriteAmount = user.getFavorites().size();
        this.wantsToPlayAmount = user.getWantsToPlay().size();
        this.hasPlayedAmount = user.getHasPlayed().size();
        this.followsAmount = user.getFollows().size();
        this.followedByAmount = user.getFollowedBy().size();
        this.reviewAmount = user.getReviews().size();
    }

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getProfilePictureURL() {
        return profilePictureURL;
    }

    public String getDescription() {
        return description;
    }

    public int getFavoriteAmount() {
        return favoriteAmount;
    }

    public int getWantsToPlayAmount() {
        return wantsToPlayAmount;
    }

    public int getHasPlayedAmount() {
        return hasPlayedAmount;
    }

    public int getFollowsAmount() {
        return followsAmount;
    }

    public int getFollowedByAmount() {
        return followedByAmount;
    }

    public int getReviewAmount() {
        return reviewAmount;
    }
}
