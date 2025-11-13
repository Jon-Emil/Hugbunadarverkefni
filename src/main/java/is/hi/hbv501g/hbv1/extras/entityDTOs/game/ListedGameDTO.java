package is.hi.hbv501g.hbv1.extras.entityDTOs.game;

import is.hi.hbv501g.hbv1.extras.entityDTOs.genre.ReferencedGenreDTO;
import is.hi.hbv501g.hbv1.persistence.entities.Game;

import java.util.ArrayList;
import java.util.List;

public class ListedGameDTO {
    private long id;
    private String title;
    private String description;
    private String releaseDate;
    private float price;
    private String coverImage;
    private String developer;
    private String publisher;

    private int reviewAmount;
    private Float averageRating;
    private int favoriteAmount;
    private int wantToPlayAmount;
    private int havePlayedAmount;

    private List<ReferencedGenreDTO> genres  = new ArrayList<>();

    public ListedGameDTO(Game game) {
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
        this.reviewAmount = game.getReviews().size();
        this.averageRating = game.getAverageRating();
        this.favoriteAmount = game.getFavoriteOf().size();
        this.wantToPlayAmount = game.getWantToPlay().size();
        this.havePlayedAmount = game.getHavePlayed().size();
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

    public int getReviewAmount() {
        return reviewAmount;
    }

    public Float getAverageRating() {
        return averageRating;
    }

    public int getFavoriteAmount() {
        return favoriteAmount;
    }

    public int getWantToPlayAmount() {
        return wantToPlayAmount;
    }

    public int getHavePlayedAmount() {
        return havePlayedAmount;
    }
}
