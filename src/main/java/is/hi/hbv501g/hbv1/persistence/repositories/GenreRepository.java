package is.hi.hbv501g.hbv1.persistence.repositories;
import is.hi.hbv501g.hbv1.persistence.entities.*;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
public interface GenreRepository extends JpaRepository<Genre, Long> {
    Genre save(Genre genre);
    List<Genre> findByTitle(String title);
    Genre findById(long id);
    List<Genre> findAll();
    void delete(Genre genre);
}
