package is.hi.hbv501g.hbv1.extras.DTOs;

import jakarta.validation.constraints.Size;

/**
 * this is a DTO that contains all the information that is possible to change excluding the profile picture
 * used when the user wants to change their account information
 */
public class UserToUpdate {
    @Size(min = 2, max = 64, message = "Username has to be between 2 and 64 characters long")
    private String username;

    @Size(min = 0, max = 1024, message = "Description has to be between 0 and 1024 characters long")
    private String description;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
