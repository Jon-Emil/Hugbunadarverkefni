package is.hi.hbv501g.hbv1.persistence.entities;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;
import javax.imageio.ImageIO;


@Entity
@Table (name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;
    private String username;
    private String passwordHash;
    //private List<User>follows;
    //private List<User>followedBy;
    //private List<Game>favorites;
    //private List<Game>wantsToPlay;
    //private List<Game>hasPlayed;
    //private List<Review> reviews;
    private String description;
    //private ImageIO profilePicture;
    private Role role;

    @OneToMany(mappedBy = "user",  cascade = CascadeType.ALL, orphanRemoval = true)
    List<Review> reviews = new ArrayList<>();

    public User() {
    }

    public User(String email, String username, String passwordHash) {
        this.email = email;
        this.username = username;
        this.passwordHash = passwordHash;
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
}
