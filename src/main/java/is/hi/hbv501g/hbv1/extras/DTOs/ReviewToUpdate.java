package is.hi.hbv501g.hbv1.extras.DTOs;

import jakarta.validation.constraints.*;

public class ReviewToUpdate {
    @Size(min = 2, max = 64, message = "Title must be between 2 and 64 characters")
    private String title;

    @Size(min = 2, max = 512, message = "Main text must be between 2 and 512 characters")
    private String text;

    @Min(value = 0, message = "Rating must be at least 0")
    @Max(value = 100, message = "Rating must be at most 100")
    private Integer rating;

    public ReviewToUpdate() {}

    public String getTitle() { return title; }
    public String getText() { return text; }
    public Integer getRating() { return rating; }

    public void setTitle(String title) { this.title = title; }
    public void setText(String text) { this.text = text; }
    public void setRating(int rating) { this.rating = rating; }

    @AssertTrue(message = "Make changes to at least one of: title, text, or rating of your review.")
    public boolean isAnyFieldProvided() {
        return title != null || text != null || rating != null;
    }

    @AssertTrue(message = "Title must not be full of spaces when provided")
    public boolean isTitleValid() {
        return title == null || !title.trim().isEmpty();
    }

    @AssertTrue(message = "Main text must not be full of spaces when provided")
    public boolean isTextValid() {
        return text == null || !text.trim().isEmpty();
    }
}