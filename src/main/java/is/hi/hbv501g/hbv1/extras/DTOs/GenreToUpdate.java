package is.hi.hbv501g.hbv1.extras.DTOs;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * A DTO used for updating a genres information and includes all information that can be changed
 * used when the user wants to change a genres information
 */
public class GenreToUpdate {
    @Size(min = 2, max = 128, message = "Title must be between 2 and 128 characters")
    private String title;

    @Size (max = 256, message = "Description must be maximum 256 characters")
    private String description;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
