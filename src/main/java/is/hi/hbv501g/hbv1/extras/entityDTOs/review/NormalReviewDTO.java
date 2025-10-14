package is.hi.hbv501g.hbv1.extras.entityDTOs.review;

import is.hi.hbv501g.hbv1.persistence.entities.Review;

public class NormalReviewDTO {
    private Long id;
    private int rating;
    private String text;
    private String title;

    public NormalReviewDTO(Review review) {
        this.id = review.getId();
        this.rating = review.getRating();
        this.text = review.getText();
        this.title = review.getTitle();
    }
}
