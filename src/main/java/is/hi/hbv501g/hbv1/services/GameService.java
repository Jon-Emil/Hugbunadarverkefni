package is.hi.hbv501g.hbv1.services;

import is.hi.hbv501g.hbv1.extras.SearchCriteria;
import is.hi.hbv501g.hbv1.persistence.entities.Game;
import is.hi.hbv501g.hbv1.persistence.entities.Review;

import java.util.List;

public interface GameService {
    Game findById(Long id);
    List<Game> findAll();
    Game findByTitle(String title);
    Game save(Game game);
    void delete(Game game);

    List<Game> search(SearchCriteria params);
    Game add(Game game, List<Long> genreIds);

    Review saveReview(Review review);
    //more to be implemented as we go on.
}
