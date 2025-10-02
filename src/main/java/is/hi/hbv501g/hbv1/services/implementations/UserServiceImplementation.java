package is.hi.hbv501g.hbv1.services.implementations;

import is.hi.hbv501g.hbv1.persistence.entities.Game;
import is.hi.hbv501g.hbv1.persistence.entities.User;
import is.hi.hbv501g.hbv1.persistence.repositories.*;
import is.hi.hbv501g.hbv1.services.*;
import jakarta.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class UserServiceImplementation implements UserService {
    private final UserRepository userRepository;
    private final GameRepository gameRepository;
    private final ReviewRepository reviewRepository;

    @Autowired
    public UserServiceImplementation(UserRepository userRepository, GameRepository gameRepository, ReviewRepository reviewRepository) {
        this.userRepository = userRepository;
        this.gameRepository = gameRepository;
        this.reviewRepository = reviewRepository;
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
    public boolean addFavouriteGames(Long gameId, Long userId) {
        User user = userRepository.findById(userId).orElse(null);
        Game game = gameRepository.findById(gameId).orElse(null);
        if (user == null || game == null) return false;

        if (user.getFavorites().stream().anyMatch(g -> g.getId().equals(gameId))) {
            return false;
        }

        
        // 拥有端：添加
        user.getFavorites().add(game);
        // 被动端：同步内存一致
        game.getFavorite_of().add(user);
        return true;
    }

    @Override
    public List<Game> getFavouriteGames(Long userId) {
        User user = userRepository.findById(userId).orElse(null);
        if (user == null) return List.of();
        return user.getFavorites().stream().toList();
    }

    @Override
    public boolean deleteFavouriteGames(Long gameId, Long userId) {
        User user = userRepository.findById(userId).orElse(null);
        Game game = gameRepository.findById(gameId).orElse(null);
        if (user == null || game == null) return false;

        boolean removed = user.getFavorites().removeIf(g -> g.getId().equals(gameId));
        if (removed) {
            game.getFavorite_of().remove(user);
        }
        return removed;
    }
}
