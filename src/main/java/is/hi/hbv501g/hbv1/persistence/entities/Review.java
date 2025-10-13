package is.hi.hbv501g.hbv1.persistence.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;

@Entity
@Table(name = "reviews")
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Review must have a rating")
    @Min(value = 0, message = "Rating must be at least 0")
    @Max(value = 100, message = "Rating must be at most 100")
    private int rating;

    @Column(length = 512)
    @NotBlank(message = "Review must have a main text")
    @Size (min = 2, max = 512, message = "Main text must be between 2 and 512 characters")
    private String text;

    @Column(length = 64)
    @NotBlank(message = "Review must have a title")
    @Size (min = 2, max = 64, message = "Title must be between 2 and 64 characters")
    private String title;

    @ManyToOne
    @JoinColumn(name = "user-id")
    @JsonIgnore
    private User user;

    @ManyToOne
    @JoinColumn(name = "game-id")
    @JsonIgnore
    private Game game;

    public Review() {
    }

    public Review(User user, Game game, int rating, String text, String title) {
        this.user = user;
        this.game = game;
        this.text = text;
        this.title = title;
        this.rating = rating;
    }

    @Transient
    public Long getUserId() {
        return user != null ? user.getId() : null;
    }

    @Transient
    public Long getGameId() {
        return game != null ? game.getId() : null;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

     //get rating
    public int getRating() {
        return rating;
    }

    //set rating
    public void setRating(int rating) {
        this.rating = rating;
    }

    //get title
    public String getTitle(){
        return title;
    }

    //set title
    public void setTitle(String title) {
        this.title = title;
    }

    // get text
    public String getText() {
        return text;
    }

    // set text
    public void setText(String text) {
        this.text = text;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }
}
