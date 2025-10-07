package is.hi.hbv501g.hbv1.persistence.repositories;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import is.hi.hbv501g.hbv1.persistence.entities.Game;
import is.hi.hbv501g.hbv1.persistence.entities.Review;

@Repository
public interface ReviewRepository 
    extends JpaRepository<Review, Long> {
    List<Review> findByGame(Game game);
    List<Review> findAll();
    Review save(Review review);
    void delete(Review review);
}

