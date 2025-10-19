package is.hi.hbv501g.hbv1.extras.DTOs;

import jakarta.validation.constraints.*;

import java.util.List;

/**
 * A DTO used for updating a games information and includes all information that can be changed excluding the coverImage
 * used when the user wants to change a games information
 */
public class GameToUpdate {
    @Size(min = 2, max = 128, message = "Title must be between 2 and 128 characters")
    private String title;

    @Size (min = 1, max = 1024, message = "Description must be between 1 and 1024 characters")
    private String description;

    @Size (min = 10, max = 16, message = "Release date should be on this format YYYY-MM-DD or DD-MM-YYYY")
    private String releaseDate;

    @Positive(message = "Price must be greater than 0")
    private Float price;

    @Size(min = 2, max = 64, message = "Developer must be between 2 and 64 char")
    private String developer;

    @Size(min = 2, max = 64, message = "Publisher must be between 2 and 64 char")
    private String publisher;

    @Size(min = 1, max = 32, message = "Game must have between 1 and 32 genres")
    private List<Long> genreIds;

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

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public Float getPrice() {
        return price;
    }

    public void setPrice(Float price) {
        this.price = price;
    }

    public String getDeveloper() {
        return developer;
    }

    public void setDeveloper(String developer) {
        this.developer = developer;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public List<Long> getGenreIds() {
        return genreIds;
    }

    public void setGenreIds(List<Long> genreIds) {
        this.genreIds = genreIds;
    }
}
