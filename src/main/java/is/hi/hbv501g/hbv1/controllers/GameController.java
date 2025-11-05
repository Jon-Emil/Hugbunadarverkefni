package is.hi.hbv501g.hbv1.controllers;

import io.jsonwebtoken.JwtException;
import is.hi.hbv501g.hbv1.extras.entityDTOs.game.NormalGameDTO;
import is.hi.hbv501g.hbv1.extras.entityDTOs.review.NormalReviewDTO;
import is.hi.hbv501g.hbv1.extras.helpers.JWTHelper;
import is.hi.hbv501g.hbv1.extras.DTOs.PaginatedResponse;
import is.hi.hbv501g.hbv1.extras.DTOs.ReviewToCreate;
import is.hi.hbv501g.hbv1.extras.DTOs.ReviewToUpdate;
import is.hi.hbv501g.hbv1.extras.DTOs.SearchCriteria;
import is.hi.hbv501g.hbv1.persistence.entities.Game;
import is.hi.hbv501g.hbv1.persistence.entities.Review;

import java.util.Comparator;

import is.hi.hbv501g.hbv1.persistence.entities.User;
import is.hi.hbv501g.hbv1.services.GameService;
import is.hi.hbv501g.hbv1.services.ReviewService;
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
public class GameController {
    private final GameService gameService;
    private final UserService userService;
    private final JWTHelper jwtHelper;
    private final ReviewService reviewService;

    @Autowired
    public GameController(
            GameService gameService,
            UserService userService,
            JWTHelper jwtHelper,
            ReviewService reviewService
    ) {
        this.gameService = gameService;
        this.userService = userService;
        this.jwtHelper = jwtHelper;
        this.reviewService = reviewService;
    }

    /**
     * Helper function used to easily get the User from the userID stored in the Auth header
     *
     * @param authHeader Where the token is stored
     * @param userNotFoundError What we want the error message to be if we don't find a user matching the userID
     *
     * @return User object of the userID that is stored in the header
     */
    private User extractUserFromHeader(String authHeader, String userNotFoundError) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new JwtException("Missing or malformed Authorization header");
        }

        String token = authHeader.substring(7); // safer than replace()
        Long userId = jwtHelper.extractUserId(token);
        User user = userService.findById(userId);
        if (user == null) throw new JwtException(userNotFoundError);
        return user;
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
    public PaginatedResponse<NormalGameDTO> allGames(
            @RequestParam(defaultValue = "1") int pageNr,
            @RequestParam(defaultValue = "10") int perPage
    ) {
        // get all games
        List<Game> allGames = gameService.findAll();

        // sort list
        allGames.sort(Comparator.comparing(Game::getTitle));

        // convert to DTOs
        List<NormalGameDTO> allGameDTOs = allGames.stream()
                .map(NormalGameDTO::new).toList();
        return new PaginatedResponse<NormalGameDTO>(200, allGameDTOs, pageNr,perPage);
    }

    @RequestMapping(value = "/games/{gameID}", method = RequestMethod.GET)
    public NormalGameDTO gameDetails(
            @PathVariable("gameID") Long gameID
    ) {
        Game game = gameService.findById(gameID);
        return new NormalGameDTO(game);
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
    public PaginatedResponse<NormalGameDTO> gameSearch(
            @RequestParam(defaultValue = "1") int pageNr,
            @RequestParam(defaultValue = "10") int perPage,
            @RequestParam(required = false) String title,
            @RequestParam(required = false) Float minPrice,
            @RequestParam(required = false) Float maxPrice,
            @RequestParam(required = false) String releasedAfter,
            @RequestParam(required = false) String releasedBefore,
            @RequestParam(required = false) String developer,
            @RequestParam(required = false) String publisher,
            @RequestParam(required = false) List<String> genres
            ) {
        SearchCriteria params = new SearchCriteria(
                title, minPrice, maxPrice, releasedAfter, releasedBefore, developer, publisher, genres
        );
        List<Game> foundGames = gameService.search(params);
        List<NormalGameDTO> allGameDTOs = foundGames.stream()
                .map(NormalGameDTO::new).toList();
        return new PaginatedResponse<NormalGameDTO>(200, allGameDTOs, pageNr,perPage);
    }

    /**
     * Handles POST requests on the /games/{gameID}/reviews
     *
     * @param authHeader Token used for authenticating users
     * @param gameID ID of the game we want to post a review to
     * @param incomingReview The contents of the review using ReviewToCreate DTO
     * @param res The validation results of the incomingReview parameters
     *
     * @return A ResponseEntity containing the code and the created NormalReviewDTO
     */
    @RequestMapping(value = "/games/{gameID}/reviews", method = RequestMethod.POST)
    // used NormalReviewDTO instead of Review object
    public ResponseEntity<NormalReviewDTO> postReview(
            @RequestHeader(value = "Authorization") String authHeader,
            @PathVariable Long gameID,
            @Valid @RequestBody ReviewToCreate incomingReview,
            BindingResult res
    ) {
        if (res.hasErrors()) {
            return ResponseEntity.badRequest().body(null);
        }

        User user;
        try {
            user = extractUserFromHeader(authHeader, "You must be logged in to add a review");
        }catch (JwtException e){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        Game game = gameService.findById(gameID);

        if (game == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        try {
            //uilized the ReviewToCreate DTO
            Review savedReview = reviewService.postReview(user, game, incomingReview);
            
            // canged saved Review entity to NormalReviewDTO for the response
            NormalReviewDTO reviewDTO = new NormalReviewDTO(savedReview);
            return ResponseEntity.status(HttpStatus.CREATED).body(reviewDTO);
        } catch( IllegalArgumentException error ) {
            return ResponseEntity.badRequest().body(null); 
        }
    }

    
    /**
     * Updating existing review for a game.
     * user must be logged in and be the author of the review
     * * Using PATCH now instead fo put like before 
     */
    @RequestMapping(value = "/games/{gameID}/reviews/{reviewID}", method=RequestMethod.PATCH)
    public ResponseEntity<String> updateReview(
        @RequestHeader(value = "Authorization") String authHeader,
        @PathVariable Long gameID,
        @PathVariable Long reviewID,
        @Valid @RequestBody ReviewToUpdate updateReviewData,
        BindingResult res
    ){
        //check for incoming review data 
        if (res.hasErrors()){
            String errors = res.getAllErrors().stream().map(error -> error.getDefaultMessage()).collect(Collectors.joining(", "));
            return ResponseEntity.badRequest().body(errors);
        }
        
        //authenticate the user
        User user;
        try{
            user = extractUserFromHeader(authHeader, "must be logged in to change review");
        } catch (JwtException e){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
        
        //then update the review
        try {
            // changed to now accept ReviewToUpdate DTO
            reviewService.updateReview(user, gameID, reviewID, updateReviewData);
            return ResponseEntity.ok().body("Review " + reviewID + " update successfully");
            
        } catch (SecurityException e) { 
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        } catch (IllegalArgumentException error) { 
            return ResponseEntity.badRequest().body(error.getMessage());
        }
    }

    /**
     * DELETE requests for a review.
     * User must be the author 
     */
    @RequestMapping(value = "/games/{gameID}/reviews/{reviewID}", method = RequestMethod.DELETE)
    public ResponseEntity<String> deleteReview(
            @RequestHeader(value = "Authorization") String authHeader,
            @PathVariable Long gameID,
            @PathVariable Long reviewID
    ) {
        User user;
        try {
            user = extractUserFromHeader(authHeader, "You must be logged in to delete a review");
        } catch (JwtException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }

        try {
            reviewService.deleteReview(user, gameID, reviewID);
            
            return ResponseEntity.ok().body("Review " + reviewID + " deleted successfully");

        } catch (SecurityException e) { //if the user is not the author
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        } catch (IllegalArgumentException error) { // if the review is not found
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error.getMessage());
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
    public ResponseEntity<String> addFavorite(
            @PathVariable("gameID") Long gameID,
            @RequestHeader(value = "Authorization") String authHeader
    ) {
        User user;
        try {
            user = extractUserFromHeader(authHeader, "You must be logged in to add a game as your favorite");
        }catch (JwtException e){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }

        Game game = gameService.findById(gameID);

        if (game == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Game not found");
        }

        try {
            userService.addFavorite(user, game);
            return ResponseEntity.ok().body("User: [" + user.getId() + "] added game: [" + gameID + "] as a favorite");
        } catch( IllegalArgumentException error ) {
            return ResponseEntity.badRequest().body(error.getMessage());
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
    public ResponseEntity<String> addWantToPlay(
            @PathVariable("gameID") Long gameID,
            @RequestHeader(value = "Authorization") String authHeader
    ) {
        User user;
        try {
            user = extractUserFromHeader(authHeader, "You must be logged in to add a game as a game you want to play");
        }catch (JwtException e){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }

        Game game = gameService.findById(gameID);

        if (game == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Game not found");
        }

        try {
            userService.addWantToPlay(user, game);
            return ResponseEntity.ok().body("User: [" + user.getId() + "] added game: [" + gameID + "] as a game they want to play");
        } catch( IllegalArgumentException error ) {
            return ResponseEntity.badRequest().body(error.getMessage());
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
    public ResponseEntity<String> addHasPlayed(
            @PathVariable("gameID") Long gameID,
            @RequestHeader(value = "Authorization") String authHeader
    ) {
        User user;
        try {
            user = extractUserFromHeader(authHeader, "You must be logged in to add a game as a game you have played");
        }catch (JwtException e){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }

        Game game = gameService.findById(gameID);

        if (game == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Game not found");
        }

        try {
            userService.addHasPlayed(user, game);
            return ResponseEntity.ok().body("User: [" + user.getId() + "] added game: [" + gameID + "] as a game they have played");
        } catch( IllegalArgumentException error ) {
            return ResponseEntity.badRequest().body(error.getMessage());
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
    public ResponseEntity<String> removeFavorite(
            @PathVariable("gameID") Long gameID,
            @RequestHeader(value = "Authorization") String authHeader
    ) {
        User user;
        try {
            user = extractUserFromHeader(authHeader, "You must be logged in to remove a game as your favorite");
        }catch (JwtException e){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }

        Game game = gameService.findById(gameID);

        if (game == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Game not found");
        }

        try {
            userService.removeFavorite(user, game);
            return ResponseEntity.ok().body("User: [" + user.getId() + "] removed game: [" + gameID + "] as a favorite");
        } catch( IllegalArgumentException error ) {
            return ResponseEntity.badRequest().body(error.getMessage());
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
    public ResponseEntity<String> removeWantToPlay(
            @PathVariable("gameID") Long gameID,
            @RequestHeader(value = "Authorization") String authHeader
    ) {
        User user;
        try {
            user = extractUserFromHeader(authHeader, "You must be logged in to remove a game as a game you want to play");
        }catch (JwtException e){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }

        Game game = gameService.findById(gameID);

        if (game == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Game not found");
        }

        try {
            userService.removeWantToPlay(user, game);
            return ResponseEntity.ok().body("User: [" + user.getId() + "] removed game: [" + gameID + "] as a game they want to play");
        } catch( IllegalArgumentException error ) {
            return ResponseEntity.badRequest().body(error.getMessage());
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
    public ResponseEntity<String> removeHasPlayed(
            @PathVariable("gameID") Long gameID,
            @RequestHeader(value = "Authorization") String authHeader
    ) {
        User user;
        try {
            user = extractUserFromHeader(authHeader, "You must be logged in to remove a game as a game you have played");
        }catch (JwtException e){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }

        Game game = gameService.findById(gameID);

        if (game == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Game not found");
        }

        try {
            userService.removeHasPlayed(user, game);
            return ResponseEntity.ok().body("User: [" + user.getId() + "] removed game: [" + gameID + "] as a game they have played");
        } catch( IllegalArgumentException error ) {
            return ResponseEntity.badRequest().body(error.getMessage());
        }
    }
}
