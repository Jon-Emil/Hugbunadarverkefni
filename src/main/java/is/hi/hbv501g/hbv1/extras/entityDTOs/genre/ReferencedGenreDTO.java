package is.hi.hbv501g.hbv1.extras.entityDTOs.genre;

import com.fasterxml.jackson.annotation.JsonBackReference;
import is.hi.hbv501g.hbv1.persistence.entities.Game;
import is.hi.hbv501g.hbv1.persistence.entities.Genre;
import jakarta.persistence.Column;
import jakarta.persistence.ManyToMany;

import java.util.ArrayList;
import java.util.List;

public class ReferencedGenreDTO {
    private long id;
    private String title;
    private String description;

    public ReferencedGenreDTO(Genre genre) {
        this.id = genre.getId();
        this.title = genre.getTitle();
        this.description = genre.getDescription();
    }
}
