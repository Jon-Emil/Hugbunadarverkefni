package is.hi.hbv501g.hbv1.services.implementations;

import is.hi.hbv501g.hbv1.persistence.entities.Game;
import is.hi.hbv501g.hbv1.persistence.repositories.GameRepository;
import is.hi.hbv501g.hbv1.services.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GameServiceImplementation implements GameService {
    private GameRepository gameRepository;

    @Autowired
    public GameServiceImplementation(GameRepository gameRepository) {
        this.gameRepository = gameRepository;
    }



    @Override
    public Game findById(Long id) {
        return gameRepository.findById(id).orElse(null);
    }

    @Override
    public List<Game> findAll() {
        return gameRepository.findAll();
    }

    @Override
    public Game findByTitle(String title) {
        //Should fix get first to return list if many games have same title
        return gameRepository.findByTitle(title).getFirst();
    }

    @Override
    public Game save(Game game) {
        return gameRepository.save(game);
    }

    @Override
    public void delete(Game game) {
        gameRepository.delete(game);
    }
}
