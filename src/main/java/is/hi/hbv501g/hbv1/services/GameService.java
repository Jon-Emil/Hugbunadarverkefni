package is.hi.hbv501g.hbv1.services;

import is.hi.hbv501g.hbv1.extras.DTOs.ReviewToUpdate;
import is.hi.hbv501g.hbv1.extras.DTOs.SearchCriteria;
import is.hi.hbv501g.hbv1.persistence.entities.Game;
import is.hi.hbv501g.hbv1.persistence.entities.Review;
import is.hi.hbv501g.hbv1.persistence.entities.User;

import java.util.List;

public interface GameService {
    Game findById(Long id);
    List<Game> findAll();
    Game findByTitle(String title);
    Game save(Game game);
    void delete(Game game);

    List<Game> search(SearchCriteria params);
    Game add(Game game, List<Long> genreIds);

    Review postReview(User user, Game game, Review incomingReview);
    Review updateReview(User user, Game game, Long reviewId, ReviewToUpdate reviewToUpdate);
    void deleteReview(User user, Game game, Long reviewId);
}
