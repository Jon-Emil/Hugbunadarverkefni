package is.hi.hbv501g.hbv1.services;
import java.util.List;
import is.hi.hbv501g.hbv1.persistence.entities.*;

public interface GenreService {
    Genre findById(Long id);
    List<Genre> findAll();
    Genre findByTitle(String title);
    Genre save(Genre game);
    void delete(Genre game);
    List<Genre> getGenresByIds(List<Long> genreIds);
}
