package is.hi.hbv501g.hbv1.persistence.entities;

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

    @ManyToMany
    @JoinTable(
        name = "user_follows",
        joinColumns = @JoinColumn(name = "follower_id"),     // the followers of the user
        inverseJoinColumns = @JoinColumn(name = "followed_id") // the users followed by the user
    )
    private List<User>follows = new ArrayList<>();

    @ManyToMany(mappedBy = "follows")
    private List<User> followedBy = new ArrayList<>();

    @ManyToMany
    @JoinTable(
        name = "user_favorites",
        joinColumns = @JoinColumn(name = "user_id"),
        inverseJoinColumns = @JoinColumn(name = "game_id")
    )
    private List<Game>favorites;

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

    private String description;
    private String profilePicttureURL;
    private Role role;

    @OneToMany(mappedBy = "user",  cascade = CascadeType.ALL, orphanRemoval = true)
    List<Review> reviews = new ArrayList<>();

    public User() {
    }

    public User(String email, String username, String passwordHash, List<User> follows, List<User>followedBy, List<Game>favorites, List<Game>wantsToPlay, List<Game>hasPlayed, String description, String profilePicttureURL, Role role) {
        this.email = email;
        this.username = username;
        this.passwordHash = passwordHash;
        this.follows = follows;
        this.followedBy = followedBy;
        this.favorites = favorites;
        this.wantsToPlay = wantsToPlay;
        this.hasPlayed = hasPlayed;
        this.description = description;
        this.profilePicttureURL = profilePicttureURL;
        this.role = role;
    }

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public List<User> getFollows() {
        return follows;
    }

    public void setFollows(List<User> follows) {
        this.follows = follows;
    }

    public List<User> getFollowedBy() {
        return followedBy;
    }

    public void setFollowedBy(List<User> followedBy) {
        this.followedBy = followedBy;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getProfilePicttureURL() {
        return profilePicttureURL;
    }

    public void setProfilePicttureURL(String profilePicttureURL) {
        this.profilePicttureURL = profilePicttureURL;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

}
