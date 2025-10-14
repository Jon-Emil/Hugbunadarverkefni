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

public class NormalUserDTO {
    private Long id;
    private String email;
    private String username;
    private String passwordHash;
    private String profilePictureURL;
    private String description;
    private Role role;

    private List<ReferencedGameDTO> favorites = new ArrayList<>();
    private List<ReferencedGameDTO> wantsToPlay = new ArrayList<>();
    private List<ReferencedGameDTO> hasPlayed = new ArrayList<>();
    private List<ReferencedReviewDTO> reviews = new ArrayList<>();

    public NormalUserDTO(User user) {
        this.id = user.getId();
        this.email = user.getEmail();
        this.username = user.getUsername();
        this.passwordHash = user.getPasswordHash();
        this.profilePictureURL = user.getProfilePictureURL();
        this.description = user.getDescription();
        this.role = user.getRole();

        this.favorites = user.getFavorites().stream()
                .map(ReferencedGameDTO::new).toList();
        this.wantsToPlay = user.getWantsToPlay().stream()
                .map(ReferencedGameDTO::new).toList();
        this.hasPlayed = user.getHasPlayed().stream()
                .map(ReferencedGameDTO::new).toList();
        this.reviews = user.getReviews().stream()
                .map(ReferencedReviewDTO::new).toList();
    }

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getUsername() {
        return username;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public String getProfilePictureURL() {
        return profilePictureURL;
    }

    public String getDescription() {
        return description;
    }

    public Role getRole() {
        return role;
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

    public List<ReferencedReviewDTO> getReviews() {
        return reviews;
    }
}
