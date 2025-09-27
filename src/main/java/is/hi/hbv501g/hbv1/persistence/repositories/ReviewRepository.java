package is.hi.hbv501g.hbv1.persistence.repositories;
import is.hi.hbv501g.hbv1.persistence.entities.*;
import java.util.List;
public interface ReviewRepository {
    List<Review> findByGame(Game game);
    List<Review> findAll();
    Review save(Review review);
    void delete(Review review);
}
