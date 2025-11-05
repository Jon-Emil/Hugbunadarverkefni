package is.hi.hbv501g.hbv1.persistence.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;

@Entity
@Table(name = "reviews",uniqueConstraints = @UniqueConstraint(columnNames = {"user-id", "game-id"}))
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 100)
    private int rating;

    @Column(length = 512)
    private String text;

    @Column(length = 64)
    private String title;

    @ManyToOne
    @JoinColumn(name = "user-id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "game-id")
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

    // get user
    public User getUser() {
        return user;
    }

    // set user
    public void setUser(User user) {
        this.user = user;
    }

    // get game
    public Game getGame() {
        return game;
    }

    // set game
    public void setGame(Game game) {
        this.game = game;
    }
}
