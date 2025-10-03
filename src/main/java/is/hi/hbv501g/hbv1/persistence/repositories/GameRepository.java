package is.hi.hbv501g.hbv1.persistence.repositories;

import is.hi.hbv501g.hbv1.persistence.entities.Game;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface GameRepository extends JpaRepository<Game, Long>, JpaSpecificationExecutor<Game> {
    Game save(Game game);
    List<Game> findByTitle(String title);
    Game findById(long id);
    List<Game> findAll();
    void delete(Game game);
}
