package is.hi.hbv501g.hbv1.extras.DTOs;

import jakarta.validation.constraints.*;

import java.util.List;

public class GameToCreate {
    @NotBlank (message = "Game must have a title")
    @Size (min = 2, max = 128, message = "Title must be between 2 and 128 characters")
    private String title;

    @NotBlank (message = "Game must have a description")
    @Size (min = 1, max = 1024, message = "Description must be between 1 and 1024 characters")
    private String description;

    @NotBlank (message = "Game must have a release date")
    @Size (min = 10, max = 16, message = "Release date should be on this format YYYY-MM-DD or DD-MM-YYYY")
    private String releaseDate;

    @NotNull(message = "Game must have a price")
    @Positive(message = "Price must be greater than 0")
    private Float price;

    @NotBlank (message = "Game must have a developer")
    @Size(min = 2, max = 64, message = "Developer must be between 2 and 64 char")
    private String developer;

    @NotBlank (message = "Game must have a publisher")
    @Size(min = 2, max = 64, message = "Publisher must be between 2 and 64 char")
    private String publisher;

    @NotEmpty (message = "A game must have at least one genre")
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
