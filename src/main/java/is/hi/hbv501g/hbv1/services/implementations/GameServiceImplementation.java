package is.hi.hbv501g.hbv1.services.implementations;

import is.hi.hbv501g.hbv1.extras.SearchCriteria;
import is.hi.hbv501g.hbv1.persistence.entities.Game;
import is.hi.hbv501g.hbv1.persistence.entities.Genre;
import is.hi.hbv501g.hbv1.persistence.repositories.GameRepository;
import is.hi.hbv501g.hbv1.persistence.repositories.GenreRepository;
import is.hi.hbv501g.hbv1.services.GameService;
import jakarta.persistence.criteria.Join;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GameServiceImplementation implements GameService {
    private GameRepository gameRepository;
    private GenreRepository genreRepository;

    @Autowired
    public GameServiceImplementation(GameRepository gameRepository,  GenreRepository genreRepository) {
        this.gameRepository = gameRepository;
        this.genreRepository = genreRepository;
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

    @Override
    public List<Game> search(SearchCriteria params) {
        // where() is currently marked as depricated but according to a stackoverflow thread there is no good replacement yet
        // meaning that we will just stick with where() and it shouldn't cause any issues for us
        Specification<Game> spec = Specification.where(null);

        String title = params.getTitle();
        if (title != null) {
            spec = spec.and((root, query, builder) ->
                    builder.like(builder.lower(root.get("title")), "%" + title.toLowerCase() + "%"));
        }

        List<String> genres = params.getGenres();
        if (genres != null && !genres.isEmpty()) {
            spec = spec.and((root, query, builder) -> {
                Join<Game, Genre> genreJoin = root.join("genres");
                return genreJoin.get("name").in(genres);
            });
        }

        Float minPrice = params.getMinPrice();
        if (minPrice != null) {
            spec = spec.and((root, query, builder) -> builder.greaterThanOrEqualTo(root.get("price"), minPrice));
        }

        Float maxPrice = params.getMaxPrice();
        if (maxPrice != null) {
            spec = spec.and((root, query, builder) -> builder.lessThanOrEqualTo(root.get("price"), maxPrice));
        }

        String releaseDateFrom = params.getReleaseDateFrom();
        if (releaseDateFrom != null) {
            spec = spec.and((root, query, builder) -> builder.greaterThanOrEqualTo(root.get("releaseDate"), releaseDateFrom));
        }

        String releaseDateTo = params.getReleaseDateTo();
        if (releaseDateTo != null) {
            spec = spec.and((root, query, builder) -> builder.lessThanOrEqualTo(root.get("releaseDate"), releaseDateTo));
        }

        String developer = params.getDeveloper();
        if (developer != null) {
            spec = spec.and((root, query, builder) ->
                    builder.like(builder.lower(root.get("developer")), "%" + developer.toLowerCase() + "%"));
        }

        String publisher = params.getPublisher();
        if (publisher != null) {
            spec = spec.and((root, query, builder) ->
                    builder.like(builder.lower(root.get("publisher")), "%" + publisher.toLowerCase() + "%"));
        }

        return gameRepository.findAll(spec);
    }

    @Override
    public Game add(Game game, List<Long> genreIds) {
        List<Genre> genres = genreRepository.findAllById(genreIds);
        game.setGenres(genres);
        return gameRepository.save(game);
    }
}
