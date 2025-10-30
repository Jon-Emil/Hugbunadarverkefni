package is.hi.hbv501g.hbv1.services.implementations;

import is.hi.hbv501g.hbv1.extras.entityDTOs.user.NormalUserDTO;
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

    /**
     * finds a user by their id
     *
     * @param id the id of the user
     *
     * @return the found user or null if no user was found
     */
    @Override
    public User findById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    /**
     * finds all users in the system
     *
     * @return a list of all users in the system
     */
    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    /**
     * finds a user by their username
     *
     * @param username the username of the user we want to find
     * @return the first user found
     */
    @Override
    public List<User> findByUsernameContaining(String username) {
        return userRepository.findByUsernameContainingIgnoreCase(username);
    }

    /**
     * saves a user to the system
     *
     * @param user the user we want to save to the system
     *
     * @return the user that was saved to the system
     */
    @Override
    public User save(User user) {
        return userRepository.save(user);
    }

    /**
     * deletes a user from the system
     *
     * @param user the user we want to delete from the system
     */
    @Override
    public void delete(User user) { userRepository.delete(user); }

    /**
     * Adds a favorite connection between a game and a user if it does not exist already
     *
     * @param user the user in the connection
     * @param game the game in the connection
     *
     * @throws IllegalArgumentException if the connection already exists
     */
    @Transactional
    @Override
    public void addFavorite(User user, Game game) {
        if (user.getFavorites().contains(game)) {
            throw new IllegalArgumentException("Connection already exists");
        }

        user.getFavorites().add(game);
        game.getFavoriteOf().add(user);

        userRepository.save(user);
        gameRepository.save(game);
    }

    /**
     * Removes the favorite connection between a game and a user
     *
     * @param user the user in the connection
     * @param game the game in the connection
     *
     * @throws IllegalArgumentException if the connection does not exist
     */
    @Transactional
    @Override
    public void removeFavorite(User user, Game game) {
        if (!user.getFavorites().contains(game)) {
            throw new IllegalArgumentException("Connection does not exist");
        }

        user.getFavorites().remove(game);
        game.getFavoriteOf().remove(user);

        userRepository.save(user);
        gameRepository.save(game);
    }

    /**
     * Adds a want to play connection between a game and a user if it does not exist already
     *
     * @param user the user in the connection
     * @param game the game in the connection
     *
     * @throws IllegalArgumentException if the connection already exists
     */
    @Transactional
    @Override
    public void addWantToPlay(User user, Game game) {
        if (user.getWantsToPlay().contains(game)) {
            throw new IllegalArgumentException("Connection already exists");
        }

        user.getWantsToPlay().add(game);
        game.getWantToPlay().add(user);

        userRepository.save(user);
        gameRepository.save(game);
    }

    /**
     * Removes the want to play connection between a game and a user
     *
     * @param user the user in the connection
     * @param game the game in the connection
     *
     * @throws IllegalArgumentException if the connection does not exist
     */
    @Transactional
    @Override
    public void removeWantToPlay(User user, Game game) {
        if (!user.getWantsToPlay().contains(game)) {
            throw new IllegalArgumentException("Connection does not exist");
        }

        user.getWantsToPlay().remove(game);
        game.getWantToPlay().remove(user);

        userRepository.save(user);
        gameRepository.save(game);
    }

    /**
     * Adds a has played connection between a game and a user if it does not exist already
     *
     * @param user the user in the connection
     * @param game the game in the connection
     *
     * @throws IllegalArgumentException if the connection already exists
     */
    @Transactional
    @Override
    public void addHasPlayed(User user, Game game) {
        if (user.getHasPlayed().contains(game)) {
            throw new IllegalArgumentException("Connection already exists");
        }

        user.getHasPlayed().add(game);
        game.getHavePlayed().add(user);

        userRepository.save(user);
        gameRepository.save(game);
    }

    /**
     * Removes the has played connection between a game and a user
     *
     * @param user the user in the connection
     * @param game the game in the connection
     *
     * @throws IllegalArgumentException if the connection does not exist
     */
    @Transactional
    @Override
    public void removeHasPlayed(User user, Game game) {
        if (!user.getHasPlayed().contains(game)) {
            throw new IllegalArgumentException("Connection does not exist");
        }

        user.getHasPlayed().remove(game);
        game.getHavePlayed().remove(user);

        userRepository.save(user);
        gameRepository.save(game);
    }

    @Override @Transactional(readOnly = true)
    public NormalUserDTO getPublicProfileById(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("User not found"));
        return new NormalUserDTO(user);
    }

    @Override
    @Transactional
    public void addFollow(User user, User userToFollow) {
        if (user.equals(userToFollow)) {
            throw new IllegalArgumentException("Users cannot follow themselves");
        }
        if (user.getFollows().contains(userToFollow)) {
            throw new IllegalArgumentException("Connection already exists");
        }

        user.getFollows().add(userToFollow);
        userToFollow.getFollowedBy().add(user);

        userRepository.save(user);
        userRepository.save(userToFollow);
    }

    @Override
    @Transactional
    public void removeFollow(User user, User userToUnfollow) {
        if (!user.getFollows().contains(userToUnfollow)) {
            throw new IllegalArgumentException("Connection does not exist");
        }

        user.getFollows().remove(userToUnfollow);
        userToUnfollow.getFollowedBy().remove(user);

        userRepository.save(user);
        userRepository.save(userToUnfollow);
    }
}
