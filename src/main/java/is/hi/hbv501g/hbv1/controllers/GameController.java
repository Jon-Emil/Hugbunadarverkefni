package is.hi.hbv501g.hbv1.controllers;

import io.jsonwebtoken.JwtException;
import is.hi.hbv501g.hbv1.extras.DTOs.NormalResponse;
import is.hi.hbv501g.hbv1.extras.entityDTOs.game.NormalGameDTO;
import is.hi.hbv501g.hbv1.extras.entityDTOs.review.NormalReviewDTO;
import is.hi.hbv501g.hbv1.extras.helpers.JWTHelper;
import is.hi.hbv501g.hbv1.extras.DTOs.PaginatedResponse;
import is.hi.hbv501g.hbv1.extras.DTOs.SearchCriteria;
import is.hi.hbv501g.hbv1.extras.helpers.SortHelper;
import is.hi.hbv501g.hbv1.persistence.entities.Game;
import is.hi.hbv501g.hbv1.persistence.entities.Genre;
import is.hi.hbv501g.hbv1.persistence.entities.Review;

import java.util.Comparator;

import is.hi.hbv501g.hbv1.persistence.entities.User;
import is.hi.hbv501g.hbv1.services.GameService;
import is.hi.hbv501g.hbv1.services.GenreService;
import is.hi.hbv501g.hbv1.services.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
public class GameController extends BaseController {
    private final GameService gameService;
    private final SortHelper sortHelper;
    private final GenreService genreService;

    @Autowired
    public GameController(
            GameService gameService,
            UserService userService,
            JWTHelper jwtHelper,
            SortHelper sortHelper,
            GenreService genreService
    ) {
        this.gameService = gameService;
        this.setUserService(userService);
        this.setJwtHelper(jwtHelper);
        this.sortHelper = sortHelper;
        this.genreService = genreService;
    }

    /**
     * Handles GET requests on /games
     *
     * @param pageNr Which page to show 1 is first page [default = 1]
     * @param perPage How many items per page [default = 10]
     *
     * @return A PaginatedResponse with a status code of 200, how many games are in total
     * and all available games sorted by their title alphabetically of the page requested
     */
    @RequestMapping(value = "/games", method = RequestMethod.GET)
    public ResponseEntity<PaginatedResponse<NormalGameDTO>> allGames(
            @RequestParam(defaultValue = "1") int pageNr,
            @RequestParam(defaultValue = "10") int perPage,
            @RequestParam(defaultValue = "title") String sortBy,
            @RequestParam(defaultValue = "false") Boolean sortReverse
    ) {
        // get all games
        List<Game> allGames = gameService.findAll();

        // sort list
        allGames = sortHelper.sortGames(allGames, sortBy, sortReverse);

        // convert to DTOs
        List<NormalGameDTO> allGameDTOs = allGames.stream()
                .map(NormalGameDTO::new).toList();
        return wrap(new PaginatedResponse<NormalGameDTO>(HttpStatus.OK.value(), allGameDTOs, pageNr,perPage));
    }

    @RequestMapping(value = "/games/{gameID}", method = RequestMethod.GET)
    public ResponseEntity<NormalResponse<NormalGameDTO>> gameDetails(
            @PathVariable("gameID") Long gameID
    ) {
        Game game = gameService.findById(gameID);
        return wrap(new NormalResponse<>(HttpStatus.OK.value(), "Game found", new NormalGameDTO(game)));
    }

    /**
     * Handles GET requests on /games/search
     *
     * @param pageNr Which page to show 1 is first page [default = 1]
     * @param perPage How many items per page [default = 10]
     * @param title Part of the title that must be present in the games [optional]
     * @param minPrice Minimum price of the games [optional]
     * @param maxPrice Maximum price of the games [optional]
     * @param releasedAfter The release date that the games must be released after in the format of YYYY-MM-DD [optional]
     * @param releasedBefore The release date that the games must be released before in the format of YYYY-MM-DD [optional]
     * @param developer Part of the developers name that must be present in the games [optional]
     * @param publisher Part of the publishers name that must be present in the games [optional]
     * @param genres Genres that the games must have [optional]
     *
     * @return A PaginatedResponse with a status code of 200, how many games are in total that match the criteria
     * and all available games that fit the criteria in the page that was requested
     */
    @RequestMapping(value = "/games/search", method = RequestMethod.GET)
    public ResponseEntity<PaginatedResponse<NormalGameDTO>> gameSearch(
            @RequestParam(defaultValue = "1") int pageNr,
            @RequestParam(defaultValue = "10") int perPage,
            @RequestParam(required = false) String title,
            @RequestParam(required = false) Float minPrice,
            @RequestParam(required = false) Float maxPrice,
            @RequestParam(required = false) String releasedAfter,
            @RequestParam(required = false) String releasedBefore,
            @RequestParam(required = false) String developer,
            @RequestParam(required = false) String publisher,
            @RequestParam(required = false) List<String> genres,
            @RequestParam(defaultValue = "title") String sortBy,
            @RequestParam(defaultValue = "false") Boolean sortReverse
            ) {
        SearchCriteria params = new SearchCriteria(
                title, minPrice, maxPrice, releasedAfter, releasedBefore, developer, publisher, genres
        );
        List<Game> foundGames = gameService.search(params);
        foundGames = sortHelper.sortGames(foundGames, sortBy, sortReverse);
        List<NormalGameDTO> allGameDTOs = foundGames.stream()
                .map(NormalGameDTO::new).toList();
        return wrap(new PaginatedResponse<NormalGameDTO>(HttpStatus.OK.value(), allGameDTOs, pageNr, perPage));
    }

    /**
     * Handles POST requests on the /games/{gameID}/reviews
     *
     * @param authHeader Token used for authenticating users
     * @param gameID ID of the game we want to post a review to
     * @param incomingReview The contents of the review
     * @param res The validation results of the incomingReview param
     *
     * @return A ResponseEntity containing the HTTP code and a body explaining what happened
     */
    @RequestMapping(value = "/games/{gameID}/reviews", method = RequestMethod.POST)
    public ResponseEntity<NormalResponse<NormalReviewDTO>> postReview(
            @RequestHeader(value = "Authorization") String authHeader,
            @PathVariable Long gameID,
            @Valid @RequestBody Review incomingReview,
            BindingResult res
    ) {
        if (res.hasErrors()) {
            String errors = res.getAllErrors().stream()
                    .map(error -> error.getDefaultMessage()).collect(Collectors.joining(", "));
            return wrap(new NormalResponse<>(HttpStatus.BAD_REQUEST.value(), errors));
        }

        User user;
        try {
            user = extractUserFromHeader(authHeader, "You must be logged in to add a review");
        }catch (JwtException e){
            return wrap(new NormalResponse<>(HttpStatus.UNAUTHORIZED.value(), e.getMessage()));
        }

        Game game = gameService.findById(gameID);

        if (game == null) {
            return wrap(new NormalResponse<>(HttpStatus.NOT_FOUND.value(), "Game not found"));
        }

        try {
            Review newReview = gameService.postReview(user, game, incomingReview);
            return wrap(new NormalResponse<NormalReviewDTO>(HttpStatus.CREATED.value(), "Review successfully added to game with id: " + gameID, new NormalReviewDTO(newReview)));
        } catch( IllegalArgumentException error ) {
            return wrap(new NormalResponse<>(HttpStatus.BAD_REQUEST.value(), error.getMessage()));
        }
    }

    /**
     * Handles POST requests on the /games/{gameID}/favorite
     *
     * @param authHeader Token used for authenticating users
     * @param gameID ID of the game we want to add as a favorite
     *
     * @return A ResponseEntity containing the HTTP code and a body explaining what happened
     */
    @RequestMapping(value = "/games/{gameID}/favorite", method = RequestMethod.POST)
    public ResponseEntity<NormalResponse<NormalGameDTO>> addFavorite(
            @PathVariable("gameID") Long gameID,
            @RequestHeader(value = "Authorization") String authHeader
    ) {
        User user;
        try {
            user = extractUserFromHeader(authHeader, "You must be logged in to add a game as your favorite");
        }catch (JwtException e){
            return wrap(new NormalResponse<>(HttpStatus.UNAUTHORIZED.value(), e.getMessage()));
        }

        Game game = gameService.findById(gameID);

        if (game == null) {
            return wrap(new NormalResponse<>(HttpStatus.NOT_FOUND.value(), "Game not found"));
        }

        try {
            userService.addFavorite(user, game);
            return wrap(new NormalResponse<>(HttpStatus.OK.value(), "[" + user.getId() + "] added game: [" + gameID + "] as a favorite", new NormalGameDTO(game)));
        } catch( IllegalArgumentException error ) {
            return wrap(new NormalResponse<>(HttpStatus.BAD_REQUEST.value(), error.getMessage()));
        }
    }

    /**
     * Handles POST requests on the /games/{gameID}/wants
     *
     * @param authHeader Token used for authenticating users
     * @param gameID ID of the game we want to add as a game we want to play
     *
     * @return A ResponseEntity containing the HTTP code and a body explaining what happened
     */
    @RequestMapping(value = "/games/{gameID}/wants", method = RequestMethod.POST)
    public ResponseEntity<NormalResponse<NormalGameDTO>> addWantToPlay(
            @PathVariable("gameID") Long gameID,
            @RequestHeader(value = "Authorization") String authHeader
    ) {
        User user;
        try {
            user = extractUserFromHeader(authHeader, "You must be logged in to add a game as a game you want to play");
        }catch (JwtException e){
            return wrap(new NormalResponse<>(HttpStatus.UNAUTHORIZED.value(), e.getMessage()));
        }

        Game game = gameService.findById(gameID);

        if (game == null) {
            return wrap(new NormalResponse<>(HttpStatus.NOT_FOUND.value(), "Game not found"));
        }

        try {
            userService.addWantToPlay(user, game);
            return wrap(new NormalResponse<>(HttpStatus.OK.value(), "User: [" + user.getId() + "] added game: [" + gameID + "] as a game they want to play", new NormalGameDTO(game)));
        } catch( IllegalArgumentException error ) {
            return wrap(new NormalResponse<>(HttpStatus.BAD_REQUEST.value(), error.getMessage()));
        }
    }

    /**
     * Handles POST requests on the /games/{gameID}/played
     *
     * @param authHeader Token used for authenticating users
     * @param gameID ID of the game we want to add as a game we have played
     *
     * @return A ResponseEntity containing the HTTP code and a body explaining what happened
     */
    @RequestMapping(value = "/games/{gameID}/played", method = RequestMethod.POST)
    public ResponseEntity<NormalResponse<NormalGameDTO>> addHasPlayed(
            @PathVariable("gameID") Long gameID,
            @RequestHeader(value = "Authorization") String authHeader
    ) {
        User user;
        try {
            user = extractUserFromHeader(authHeader, "You must be logged in to add a game as a game you have played");
        }catch (JwtException e){
            return wrap(new NormalResponse<>(HttpStatus.UNAUTHORIZED.value(), e.getMessage()));
        }

        Game game = gameService.findById(gameID);

        if (game == null) {
            return wrap(new NormalResponse<>(HttpStatus.NOT_FOUND.value(), "Game not found"));
        }

        try {
            userService.addHasPlayed(user, game);
            return wrap(new NormalResponse<>(HttpStatus.OK.value(), "User: [" + user.getId() + "] added game: [" + gameID + "] as a game they have played", new NormalGameDTO(game)));
        } catch( IllegalArgumentException error ) {
            return wrap(new NormalResponse<>(HttpStatus.BAD_REQUEST.value(), error.getMessage()));
        }
    }

    /**
     * Handles DELETE requests on the /games/{gameID}/favorite
     *
     * @param authHeader Token used for authenticating users
     * @param gameID ID of the game we want to remove as our favorite
     *
     * @return A ResponseEntity containing the HTTP code and a body explaining what happened
     */
    @RequestMapping(value = "/games/{gameID}/favorite", method = RequestMethod.DELETE)
    public ResponseEntity<NormalResponse<NormalGameDTO>> removeFavorite(
            @PathVariable("gameID") Long gameID,
            @RequestHeader(value = "Authorization") String authHeader
    ) {
        User user;
        try {
            user = extractUserFromHeader(authHeader, "You must be logged in to remove a game as your favorite");
        }catch (JwtException e){
            return wrap(new NormalResponse<>(HttpStatus.UNAUTHORIZED.value(), e.getMessage()));
        }

        Game game = gameService.findById(gameID);

        if (game == null) {
            return wrap(new NormalResponse<>(HttpStatus.NOT_FOUND.value(), "Game not found"));
        }

        try {
            userService.removeFavorite(user, game);
            return wrap(new NormalResponse<>(HttpStatus.OK.value(), "User: [" + user.getId() + "] removed game: [" + gameID + "] as a favorite", new NormalGameDTO(game)));
        } catch( IllegalArgumentException error ) {
            return wrap(new NormalResponse<>(HttpStatus.BAD_REQUEST.value(), error.getMessage()));
        }
    }

    /**
     * Handles DELETE requests on the /games/{gameID}/wants
     *
     * @param authHeader Token used for authenticating users
     * @param gameID ID of the game we want to remove as a game we want to play
     *
     * @return A ResponseEntity containing the HTTP code and a body explaining what happened
     */
    @RequestMapping(value = "/games/{gameID}/wants", method = RequestMethod.DELETE)
    public ResponseEntity<NormalResponse<NormalGameDTO>> removeWantToPlay(
            @PathVariable("gameID") Long gameID,
            @RequestHeader(value = "Authorization") String authHeader
    ) {
        User user;
        try {
            user = extractUserFromHeader(authHeader, "You must be logged in to remove a game as a game you want to play");
        }catch (JwtException e){
            return wrap(new NormalResponse<>(HttpStatus.UNAUTHORIZED.value(), e.getMessage()));
        }

        Game game = gameService.findById(gameID);

        if (game == null) {
            return wrap(new NormalResponse<>(HttpStatus.NOT_FOUND.value(), "Game not found"));
        }

        try {
            userService.removeWantToPlay(user, game);
            return wrap(new NormalResponse<>(HttpStatus.OK.value(), "User: [" + user.getId() + "] removed game: [" + gameID + "] as a game they want to play", new NormalGameDTO(game)));
        } catch( IllegalArgumentException error ) {
            return wrap(new NormalResponse<>(HttpStatus.BAD_REQUEST.value(), error.getMessage()));
        }
    }

    /**
     * Handles DELETE requests on the /games/{gameID}/played
     *
     * @param authHeader Token used for authenticating users
     * @param gameID ID of the game we want to remove as a game we have played
     *
     * @return A ResponseEntity containing the HTTP code and a body explaining what happened
     */
    @RequestMapping(value = "/games/{gameID}/played", method = RequestMethod.DELETE)
    public ResponseEntity<NormalResponse<NormalGameDTO>> removeHasPlayed(
            @PathVariable("gameID") Long gameID,
            @RequestHeader(value = "Authorization") String authHeader
    ) {
        User user;
        try {
            user = extractUserFromHeader(authHeader, "You must be logged in to remove a game as a game you have played");
        }catch (JwtException e){
            return wrap(new NormalResponse<>(HttpStatus.UNAUTHORIZED.value(), e.getMessage()));
        }

        Game game = gameService.findById(gameID);

        if (game == null) {
            return wrap(new NormalResponse<>(HttpStatus.NOT_FOUND.value(), "Game not found"));
        }

        try {
            userService.removeHasPlayed(user, game);
            return wrap(new NormalResponse<>(HttpStatus.OK.value(), "User: [" + user.getId() + "] removed game: [" + gameID + "] as a game they have played", new NormalGameDTO(game)));
        } catch( IllegalArgumentException error ) {
            return wrap(new NormalResponse<>(HttpStatus.BAD_REQUEST.value(), error.getMessage()));
        }
    }

    @GetMapping("/games/genre/{genreId}")
    public ResponseEntity<PaginatedResponse<NormalGameDTO>> listGamesByGenreId(
            @PathVariable Long genreId,
            @RequestParam(defaultValue = "1") int pageNr,
            @RequestParam(defaultValue = "10") int perPage
    ) {
        Genre genre = genreService.findById(genreId);
        if (genre == null) {
            throw new org.springframework.web.server.ResponseStatusException(
                    org.springframework.http.HttpStatus.NOT_FOUND, "Genre not found");
        }

        List<Game> allGames = genre.getGames();
        if (allGames == null) allGames = List.of();

        allGames.sort(Comparator.comparing((Game g) -> {
            String t = g.getTitle();
            return t == null ? "" : t.toLowerCase();
        }).thenComparing(g -> g.getTitle() == null ? "" : g.getTitle())
                .thenComparingLong(Game::getId));

        int page = Math.max(pageNr, 1);
        int per  = Math.max(perPage, 1);
        int from = Math.min((page - 1) * per, allGames.size());
        int to   = Math.min(from + per, allGames.size());

        List<NormalGameDTO> pageDTOs = allGames.subList(from, to).stream().map(NormalGameDTO::new).toList();

        return wrap(new PaginatedResponse<>(200, pageDTOs, page, per));
    }
}
