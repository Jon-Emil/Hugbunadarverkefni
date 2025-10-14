package is.hi.hbv501g.hbv1.extras.entityDTOs.genre;

import is.hi.hbv501g.hbv1.extras.entityDTOs.game.ReferencedGameDTO;
import is.hi.hbv501g.hbv1.persistence.entities.Game;
import is.hi.hbv501g.hbv1.persistence.entities.Genre;

import java.util.ArrayList;
import java.util.List;

public class NormalGenreDTO {
    private long id;
    private String title;
    private String description;

    private List<ReferencedGameDTO> games  = new ArrayList<>();

    public NormalGenreDTO(Genre genre) {
        this.id = genre.getId();
        this.title = genre.getTitle();
        this.description = genre.getDescription();

        this.games = genre.getGames().stream()
                .map(ReferencedGameDTO::new).toList();
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

    public List<ReferencedGameDTO> getGames() {
        return games;
    }
}
