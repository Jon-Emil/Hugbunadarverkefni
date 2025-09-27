package is.hi.hbv501g.hbv1.services;
import java.util.List;
import is.hi.hbv501g.hbv1.persistence.entities.*;

public interface GenreService {
    List<Genre> getAllGenres();
    List<Genre> getGenresByTitle(String title);
    Genre createGenre(Genre genre);
    Genre updateGenre(Genre genre, Long id);
    void deleteGenre(Long id);
}
