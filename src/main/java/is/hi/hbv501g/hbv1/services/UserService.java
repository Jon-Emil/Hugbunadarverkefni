package is.hi.hbv501g.hbv1.services;

import is.hi.hbv501g.hbv1.extras.entityDTOs.user.NormalUserDTO;
import is.hi.hbv501g.hbv1.persistence.entities.Game;
import is.hi.hbv501g.hbv1.persistence.entities.User;

import java.util.List;

public interface UserService {
    User findById(Long id);
    List<User> findAll();
    User findByUsername(String username);
    User save(User user);
    void delete(User user);
    void addFavorite(User user, Game game);
    void addHasPlayed(User user, Game game);
    void addWantToPlay(User user, Game game);
    void removeFavorite(User user, Game game);
    void removeHasPlayed(User user, Game game);
    void removeWantToPlay(User user, Game game);

    NormalUserDTO getPublicProfileById(Long userId);

}
