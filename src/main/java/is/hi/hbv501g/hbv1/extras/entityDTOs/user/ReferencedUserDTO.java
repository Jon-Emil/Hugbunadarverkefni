package is.hi.hbv501g.hbv1.extras.entityDTOs.user;

import is.hi.hbv501g.hbv1.persistence.entities.Role;
import is.hi.hbv501g.hbv1.persistence.entities.User;

public class ReferencedUserDTO {
    private Long id;
    private String email;
    private String username;
    private String passwordHash;
    private String profilePictureURL;
    private String description;
    private Role role;

    public ReferencedUserDTO(User user) {
        this.id = user.getId();
        this.email = user.getEmail();
        this.username = user.getUsername();
        this.passwordHash = user.getPasswordHash();
        this.profilePictureURL = user.getProfilePictureURL();
        this.description = user.getDescription();
        this.role = user.getRole();
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
}
