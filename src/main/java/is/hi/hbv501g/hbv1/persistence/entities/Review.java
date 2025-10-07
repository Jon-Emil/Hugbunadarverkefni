package is.hi.hbv501g.hbv1.persistence.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "reviews")
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int rating;

     @Column(columnDefinition = "TEXT")
    private String text;

    private String title;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
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
