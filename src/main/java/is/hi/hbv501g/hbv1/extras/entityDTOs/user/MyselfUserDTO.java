package is.hi.hbv501g.hbv1.extras.entityDTOs.user;

import is.hi.hbv501g.hbv1.extras.entityDTOs.game.ReferencedGameDTO;
import is.hi.hbv501g.hbv1.extras.entityDTOs.review.ReferencedReviewDTO;
import is.hi.hbv501g.hbv1.persistence.entities.Role;
import is.hi.hbv501g.hbv1.persistence.entities.User;

import java.util.ArrayList;
import java.util.List;

/**
 * this is a DTO containing all information about a user just like the NormalUserDTO
 * but also sensitive information like role and email
 * this is meant to be only used when sending a user information about the account they are logged into
 */
public class MyselfUserDTO {
    private Long id;
    private String email;
    private String username;
    private String profilePictureURL;
    private String description;
    private Role role;

    private List<ReferencedUserDTO> follows = new ArrayList<>();
    private List<ReferencedUserDTO> followedBy =  new ArrayList<>();
    private List<ReferencedGameDTO> favorites =  new ArrayList<>();
    private List<ReferencedGameDTO> wantsToPlay = new ArrayList<>();
    private List<ReferencedGameDTO> hasPlayed = new ArrayList<>();
    private List<ReferencedReviewDTO> reviews = new ArrayList<>();

    public MyselfUserDTO(User user) {
        this.id = user.getId();
        this.email =  user.getEmail();
        this.username = user.getUsername();
        this.profilePictureURL = user.getProfilePictureURL();
        this.description = user.getDescription();
        this.role = user.getRole();

        this.favorites = user.getFavorites().stream().map(ReferencedGameDTO::new).toList();
        this.wantsToPlay = user.getWantsToPlay().stream().map(ReferencedGameDTO::new).toList();
        this.hasPlayed = user.getHasPlayed().stream().map(ReferencedGameDTO::new).toList();
        this.followedBy = user.getFollowedBy().stream().map(ReferencedUserDTO::new).toList();
        this.follows = user.getFollows().stream().map(ReferencedUserDTO::new).toList();
        this.reviews = user.getReviews().stream().map(ReferencedReviewDTO::new).toList();
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
    public String getProfilePictureURL() {
        return profilePictureURL;
    }
    public String getDescription() {
        return description;
    }
    public Role getRole() {
        return role;
    }
    public List<ReferencedUserDTO> getFollows() {
        return follows;
    }
    public List<ReferencedUserDTO> getFollowedBy() {
        return followedBy;
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
    public List<ReferencedReviewDTO> getReviews() { return reviews; }
}
