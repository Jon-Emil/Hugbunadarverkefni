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

/**
 * this is a DTO that is used as the first layer when we provide a user with information about a user
 * and it contains nearly all the info of a user including the lists of other objects but they are Referenced DTOs
 * so they wont go in an infinite loop
 * does not contain the email or hashed password of the user
 */
public class NormalUserDTO {
    private Long id;
    private String username;
    private String profilePictureURL;
    private String description;

    private List<ReferencedGameDTO> favorites = new ArrayList<>();
    private List<ReferencedGameDTO> wantsToPlay = new ArrayList<>();
    private List<ReferencedGameDTO> hasPlayed = new ArrayList<>();
    private List<ReferencedUserDTO> follows = new ArrayList<>();
    private List<ReferencedUserDTO> followedBy = new ArrayList<>();

    public NormalUserDTO(User user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.profilePictureURL = user.getProfilePictureURL();
        this.description = user.getDescription();

        this.favorites = user.getFavorites().stream()
                .map(ReferencedGameDTO::new).toList();
        this.wantsToPlay = user.getWantsToPlay().stream()
                .map(ReferencedGameDTO::new).toList();
        this.hasPlayed = user.getHasPlayed().stream()
                .map(ReferencedGameDTO::new).toList();
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

    public List<ReferencedGameDTO> getFavorites() {
        return favorites;
    }

    public List<ReferencedGameDTO> getWantsToPlay() {
        return wantsToPlay;
    }

    public List<ReferencedGameDTO> getHasPlayed() {
        return hasPlayed;
    }

    public List<ReferencedUserDTO> getFollows() { return follows; }

    public List<ReferencedUserDTO> getFollowedBy() { return followedBy; }
}
