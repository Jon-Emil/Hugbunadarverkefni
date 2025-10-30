package is.hi.hbv501g.hbv1.persistence.repositories;

import is.hi.hbv501g.hbv1.persistence.entities.Game;
import is.hi.hbv501g.hbv1.persistence.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    User save(User user);
    List<User> findByUsernameContainingIgnoreCase(String username);
    User findByEmail(String email);
    User findById(long id);
    List<User> findAll();
    void delete(User user);
}
