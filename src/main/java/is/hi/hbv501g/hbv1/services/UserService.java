package is.hi.hbv501g.hbv1.services;

import is.hi.hbv501g.hbv1.persistence.entities.User;

import java.util.List;

public interface UserService {
    User findById(Long id);
    List<User> findAll();
    User findByUsername(String username);
    User save(User user);
    void delete(User user);
    boolean toggleFavorite(Long userId, Long gameId);
    boolean toggleWantToPlay(Long userId, Long gameId);
    boolean toggleHasPlayed(Long userId, Long gameId);

}
