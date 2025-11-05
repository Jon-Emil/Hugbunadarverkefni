package is.hi.hbv501g.hbv1.extras.DTOs;

import jakarta.validation.constraints.*;

public class ReviewToCreate {
    @NotBlank(message = "Review must have a title")
    @Size(min = 2, max = 64, message = "Title must be between 2 and 64 characters")
    private String title;

    @NotBlank(message = "Review must have a main text")
    @Size(min = 2, max = 512, message = "Main text must be between 2 and 512 characters")
    private String text;

    @NotNull(message = "Review must have a rating")
    @Min(value = 0, message = "Rating must be at least 0")
    @Max(value = 100, message = "Rating must be at most 100")
    private int rating;

    public ReviewToCreate() {}

    public String getTitle() { return title; }
    public String getText() { return text; }
    public int getRating() { return rating; }

    public void setTitle(String title) { this.title = title; }
    public void setText(String text) { this.text = text; }
    public void setRating(int rating) { this.rating = rating; }

}