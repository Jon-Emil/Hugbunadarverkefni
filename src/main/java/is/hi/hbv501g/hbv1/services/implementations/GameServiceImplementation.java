package is.hi.hbv501g.hbv1.services.implementations;

import is.hi.hbv501g.hbv1.extras.DTOs.SearchCriteria;
import is.hi.hbv501g.hbv1.persistence.entities.Game;
import is.hi.hbv501g.hbv1.persistence.entities.Genre;
import is.hi.hbv501g.hbv1.persistence.entities.Review;
import is.hi.hbv501g.hbv1.persistence.entities.User;
import is.hi.hbv501g.hbv1.persistence.repositories.GameRepository;
import is.hi.hbv501g.hbv1.persistence.repositories.GenreRepository;
import is.hi.hbv501g.hbv1.persistence.repositories.ReviewRepository;
import is.hi.hbv501g.hbv1.services.GameService;
import jakarta.persistence.criteria.Join;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
public class GameServiceImplementation implements GameService {
    private final GameRepository gameRepository;
    private final GenreRepository genreRepository;
    private final ReviewRepository reviewRepository;

    @Autowired
    public GameServiceImplementation(
            GameRepository gameRepository,
            GenreRepository genreRepository,
            ReviewRepository reviewRepository
    ) {
        this.gameRepository = gameRepository;
        this.genreRepository = genreRepository;
        this.reviewRepository = reviewRepository;
    }


    /**
     * finds a game by its id
     *
     * @param id the id of the game
     *
     * @return the game that was found and if no game was found returns null
     */
    @Override
    public Game findById(Long id) {
        return gameRepository.findById(id).orElse(null);
    }

    /**
     * finds all games in the system
     *
     * @return all games in the system as a List<Game> object
     */
    @Override
    public List<Game> findAll() {
        return gameRepository.findAll();
    }

    /**
     * finds a game by its title
     *
     * @param title the title of the game
     *
     * @return the first game it finds with that title
     */
    @Override
    public Game findByTitle(String title) {
        //Should fix get first to return list if many games have same title
        return gameRepository.findByTitle(title).getFirst();
    }

    /**
     * saves a game to the system
     *
     * @param game the game object that is supposed to be saved
     *
     * @return the game that was saved
     */
    @Override
    public Game save(Game game) {
        return gameRepository.save(game);
    }

    /**
     * deletes a game from the system
     *
     * @param game the game that is supposed to be deleted
     */
    @Override
    public void delete(Game game) {
        gameRepository.delete(game);
    }

    /**
     * finds all games in the system that meet the search parameters
     *
     * @param params a SearchCriteria object containing all the search parameters
     *
     * @return a list of all games that matched the search parameters
     */
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
                return genreJoin.get("title").in(genres);
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

    /**
     * adds a list of genres to the specified game
     *
     * @param game the game that the genres are to be added to
     * @param genreIds the list of ids of the genres
     *
     * @return the new game object
     */
    @Override
    public Game add(Game game, List<Long> genreIds) {
        List<Genre> genres = genreRepository.findAllById(genreIds);
        game.setGenres(genres);
        return gameRepository.save(game);
    }

    /**
     * adds a review from a user to a specific game
     *
     * @param user A user that exists in the system and who the review will be linked to
     * @param game A game that exists in the system and what the review will be linked to
     * @param incomingReview The rest of the information about the review
     *
     * @return The review that was added to the system
     */
    @Override
    @Transactional
    public Review postReview(User user, Game game, Review incomingReview) {
        Optional<Review> existingReview = reviewRepository.findByGameAndUser(game, user);

        if (existingReview.isPresent()) {
            throw new IllegalArgumentException("Review already exists for this user and game");
        }

        Review review = new Review();
        review.setRating(incomingReview.getRating());
        review.setTitle(incomingReview.getTitle());
        review.setText(incomingReview.getText());
        review.setUser(user);
        review.setGame(game);

        return reviewRepository.save(review);
    }

    @Override
    public List<Game> listAllByGenreIdSorted(Long genreId) {
        if(!genreRepository.existsById(genreId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Genre not found: " +  genreId);
        }
        return gameRepository.findDistinctByGenres_Id(genreId, Sort.by(Sort.Direction.ASC, "title"));
    }
}
