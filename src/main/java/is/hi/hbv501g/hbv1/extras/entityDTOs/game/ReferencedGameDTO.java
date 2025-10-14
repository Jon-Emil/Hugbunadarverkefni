package is.hi.hbv501g.hbv1.extras.entityDTOs.game;

import is.hi.hbv501g.hbv1.persistence.entities.Game;

public class ReferencedGameDTO {
    private long id;
    private String title;
    private String description;
    private String releaseDate;
    private float price;
    private String coverImage;
    private String developer;
    private String publisher;

    public ReferencedGameDTO(Game game) {
        this.id = game.getId();
        this.title = game.getTitle();
        this.description = game.getDescription();
        this.releaseDate = game.getReleaseDate();
        this.price = game.getPrice();
        this.coverImage = game.getCoverImage();
        this.developer = game.getDeveloper();
        this.publisher = game.getPublisher();
    }
}
