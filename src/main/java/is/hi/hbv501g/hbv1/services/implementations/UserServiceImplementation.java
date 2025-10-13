package is.hi.hbv501g.hbv1.services.implementations;

import is.hi.hbv501g.hbv1.persistence.entities.User;
import is.hi.hbv501g.hbv1.persistence.entities.Game;
import is.hi.hbv501g.hbv1.persistence.repositories.GameRepository;
import is.hi.hbv501g.hbv1.persistence.repositories.UserRepository;
import is.hi.hbv501g.hbv1.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserServiceImplementation implements UserService {
    private final UserRepository userRepository;
    private final GameRepository gameRepository;

    @Autowired
    public UserServiceImplementation(UserRepository userRepository, GameRepository gameRepository) {
        this.userRepository = userRepository;
        this.gameRepository = gameRepository;
    }

    @Override
    public User findById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public User findByUsername(String username) {
        return userRepository.findByUsername(username).getFirst();
    }

    @Override
    public User save(User user) {
        return userRepository.save(user);
    }

    @Override
    public void delete(User user) { userRepository.delete(user); }

    @Override
    @Transactional
    public boolean toggleFavorite(Long userId, Long gameId) {
        User user = userRepository.findById(userId).orElseThrow();
        Game game = gameRepository.findById(gameId).orElseThrow();

        boolean added;
        if (user.getFavorites().contains(game)) {
            user.getFavorites().remove(game);
            added = false;
        } else {
            user.getFavorites().add(game);
            added = true;
        }
        userRepository.save(user);
        return added; // true þýðir að leikurinn hefur verið officially favoritaður af aðferðinni, false þýðir að leikurinn hefur verið officially af-favoritaður af aðferðinni.
    }

    @Override
    @Transactional
    public boolean toggleWantToPlay(Long userId, Long gameId) {
        User user = userRepository.findById(userId).orElseThrow();
        Game game = gameRepository.findById(gameId).orElseThrow();

        boolean added;
        if (user.getWantsToPlay().contains(game)) {
            user.getWantsToPlay().remove(game);
            added = false;
        } else {
            user.getWantsToPlay().add(game);
            added = true;
        }
        userRepository.save(user);
        return added; // skoða komment af toggleFav, sambærilegt
    }

    @Override
    @Transactional
    public boolean toggleHasPlayed(Long userId, Long gameId) {
        User user = userRepository.findById(userId).orElseThrow();
        Game game = gameRepository.findById(gameId).orElseThrow();

        boolean added;
        if (user.getHasPlayed().contains(game)) {
            user.getHasPlayed().remove(game);
            added = false;
        } else {
            user.getHasPlayed().add(game);
            added = true;
        }
        userRepository.save(user);
        return added; // skoða komment af toggleFav, sambærilegt.
    }

}
