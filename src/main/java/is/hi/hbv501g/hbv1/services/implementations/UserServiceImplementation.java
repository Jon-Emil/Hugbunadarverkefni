package is.hi.hbv501g.hbv1.services.implementations;

import is.hi.hbv501g.hbv1.persistence.entities.Game;
import is.hi.hbv501g.hbv1.persistence.entities.User;
import is.hi.hbv501g.hbv1.persistence.repositories.*;
import is.hi.hbv501g.hbv1.services.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public boolean addFavouriteGames(Long gameID, Long userID) {
        User u = userRepository.findById(userID).orElse(null);
        Game g = gameRepository.findById(gameID).orElse(null);
        if (u == null || g == null) return false;

        // 按 ID 检查是否已存在（不依赖 equals/hashCode）
        boolean exists = u.getFavorites().stream().anyMatch(x -> x.getId().equals(gameID));
        if (exists) return false;

        // 拥有端更新（写入中间表）
        u.getFavorites().add(g);
        // 被动端仅做内存一致（不做也行，但做了更一致）
        g.getFavorite_of().add(u);
        return true;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Game> getFavouriteGames(Long userID) {
        User u = userRepository.findById(userID).orElse(null);
        if (u == null) return List.of();
        // 若怕懒加载在 controller 序列化时报错，可改为返回副本：return new ArrayList<>(u.getFavorites());
        return u.getFavorites();
    }

    @Override
    public boolean deleteFavouriteGames(Long gameID, Long userID) {
        User u = userRepository.findById(userID).orElse(null);
        Game g = gameRepository.findById(gameID).orElse(null);
        if (u == null || g == null) return false;

        // 只按 ID 比较移除，避免依赖 equals/hashCode
        boolean removed = u.getFavorites().removeIf(x -> x.getId().equals(gameID));
        if (removed) {
            g.getFavorite_of().removeIf(us -> us.getId().equals(userID)); // 保持内存一致
        }
        return removed;
    }
}
