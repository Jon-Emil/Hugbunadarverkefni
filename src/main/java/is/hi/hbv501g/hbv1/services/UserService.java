package is.hi.hbv501g.hbv1.services;

import is.hi.hbv501g.hbv1.persistence.entities.Game;
import is.hi.hbv501g.hbv1.persistence.entities.User;

import java.util.List;

public interface UserService {
    User findById(Long id);
    List<User> findAll();
    User findByUsername(String username);
    User save(User user);
    void delete(User user);
    boolean addFavouriteGames(Long gameId, Long userId);
    List<Game> getFavouriteGames(Long userId);
    boolean deleteFavouriteGames(Long gameId, Long userId);
}
