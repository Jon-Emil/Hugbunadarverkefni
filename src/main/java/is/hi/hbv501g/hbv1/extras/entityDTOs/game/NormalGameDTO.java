package is.hi.hbv501g.hbv1.extras.entityDTOs.game;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import is.hi.hbv501g.hbv1.extras.entityDTOs.genre.ReferencedGenreDTO;
import is.hi.hbv501g.hbv1.extras.entityDTOs.review.ReferencedReviewDTO;
import is.hi.hbv501g.hbv1.extras.entityDTOs.user.ReferencedUserDTO;
import is.hi.hbv501g.hbv1.persistence.entities.Game;
import is.hi.hbv501g.hbv1.persistence.entities.Genre;
import is.hi.hbv501g.hbv1.persistence.entities.Review;
import is.hi.hbv501g.hbv1.persistence.entities.User;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

public class NormalGameDTO {
    private long id;
    private String title;
    private String description;
    private String releaseDate;
    private float price;
    private String coverImage;
    private String developer;
    private String publisher;

    private List<ReferencedGenreDTO> genres  = new ArrayList<>();
    private List<ReferencedReviewDTO> reviews = new ArrayList<>();
    private List<ReferencedUserDTO> favoriteOf = new ArrayList<>();
    private List<ReferencedUserDTO> wantToPlay = new ArrayList<>();
    private List<ReferencedUserDTO> havePlayed = new ArrayList<>();

    public NormalGameDTO(Game game) {
        this.id = game.getId();
        this.title = game.getTitle();
        this.description = game.getDescription();
        this.releaseDate = game.getReleaseDate();
        this.price = game.getPrice();
        this.coverImage = game.getCoverImage();
        this.developer = game.getDeveloper();
        this.publisher = game.getPublisher();

        this.genres = game.getGenres().stream()
                .map(ReferencedGenreDTO::new).toList();
        this.reviews = game.getReviews().stream()
                .map(ReferencedReviewDTO::new).toList();
        this.favoriteOf = game.getFavoriteOf().stream()
                .map(ReferencedUserDTO::new).toList();
        this.wantToPlay = game.getWantToPlay().stream()
                .map(ReferencedUserDTO::new).toList();
        this.havePlayed = game.getHavePlayed().stream()
                .map(ReferencedUserDTO::new).toList();
    }

    public long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public float getPrice() {
        return price;
    }

    public String getCoverImage() {
        return coverImage;
    }

    public String getDeveloper() {
        return developer;
    }

    public String getPublisher() {
        return publisher;
    }

    public List<ReferencedGenreDTO> getGenres() {
        return genres;
    }

    public List<ReferencedReviewDTO> getReviews() {
        return reviews;
    }

    public List<ReferencedUserDTO> getFavoriteOf() {
        return favoriteOf;
    }

    public List<ReferencedUserDTO> getWantToPlay() {
        return wantToPlay;
    }

    public List<ReferencedUserDTO> getHavePlayed() {
        return havePlayed;
    }
}
