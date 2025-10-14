package is.hi.hbv501g.hbv1.persistence.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table (name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;
    private String username;
    private String passwordHash;
    private String profilePictureURL;
    private String description;
    private Role role;
    //private List<User>follows;
    //private List<User>followedBy;
    @ManyToMany
    @JoinTable(
            name = "user_favorites",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "game_id")
    )
    private List<Game> favorites = new ArrayList<>();

    @ManyToMany
    @JoinTable(
            name = "user_wants_to_play",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "game_id")
    )
    private List<Game> wantsToPlay = new ArrayList<>();

    @ManyToMany
    @JoinTable(
            name = "user_has_played",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "game_id")
    )
    private List<Game> hasPlayed = new ArrayList<>();

    @OneToMany(mappedBy = "user",  cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Review> reviews = new ArrayList<>();

    public User() {
    }

    public User(String email, String username, String passwordHash) {
        this.email = email;
        this.username = username;
        this.passwordHash = passwordHash;
        this.role = Role.USER;
    }

    public User(String email, String username, String passwordHash, String description, String profilePictureURL, List<Game> favorites, List<Game> wantsToPlay, List<Game> hasPlayed) {
        this.email = email;
        this.username = username;
        this.passwordHash = passwordHash;
        this.description = description;
        this.profilePictureURL = profilePictureURL;
        this.role = Role.USER;
        this.favorites = favorites;
        this.wantsToPlay = wantsToPlay;
        this.hasPlayed = hasPlayed;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Role getRole() {
        return role;
    }

    public List<Review> getReviews() {
        return reviews;
    }

    public void setReviews(List<Review> reviews) {
        this.reviews = reviews;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getProfilePictureURL() {
        return profilePictureURL;
    }

    public void setProfilePictureURL(String profilePictureURL) {
        this.profilePictureURL = profilePictureURL;
    }

    public List<Game> getFavorites() {
        return favorites;
    }

    public void setFavorites(List<Game> favorites) {
        this.favorites = favorites;
    }

    public List<Game> getWantsToPlay() {
        return wantsToPlay;
    }

    public void setWantsToPlay(List<Game> wantsToPlay) {
        this.wantsToPlay = wantsToPlay;
    }

    public List<Game> getHasPlayed() {
        return hasPlayed;
    }

    public void setHasPlayed(List<Game> hasPlayed) {
        this.hasPlayed = hasPlayed;
    }

}
