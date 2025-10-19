package is.hi.hbv501g.hbv1.extras.entityDTOs.user;

import is.hi.hbv501g.hbv1.extras.entityDTOs.game.ReferencedGameDTO;
import is.hi.hbv501g.hbv1.persistence.entities.Role;
import is.hi.hbv501g.hbv1.persistence.entities.User;

import java.util.List;

/**
 * this is a DTO that is used as the second layer when we provide a user with information about a user
 * so it includes all info about a user excluding references to other objects to avoid infinite recursion
 */
public class ReferencedUserDTO {
    private Long id;
    private String username;
    private String profilePictureURL;
    private String description;

    public ReferencedUserDTO(User user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.profilePictureURL = user.getProfilePictureURL();
        this.description = user.getDescription();
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

}
