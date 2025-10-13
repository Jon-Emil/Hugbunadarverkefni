package is.hi.hbv501g.hbv1.controllers;

import io.jsonwebtoken.JwtException;
import is.hi.hbv501g.hbv1.extras.JWTHelper;
import is.hi.hbv501g.hbv1.extras.PaginatedResponse;
import is.hi.hbv501g.hbv1.extras.SearchCriteria;
import is.hi.hbv501g.hbv1.persistence.entities.Game;
import is.hi.hbv501g.hbv1.persistence.entities.Review;
import is.hi.hbv501g.hbv1.persistence.entities.Role;
import java.util.Map;
import is.hi.hbv501g.hbv1.persistence.entities.User;
import is.hi.hbv501g.hbv1.services.GameService;
import is.hi.hbv501g.hbv1.services.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
public class GameController {
    private final GameService gameService;
    private final UserService userService;
    private final JWTHelper jwtHelper;

    @Autowired
    public GameController(
            GameService gameService,
            UserService userService,
            JWTHelper jwtHelper
    ) {
        this.gameService = gameService;
        this.userService = userService;
        this.jwtHelper = jwtHelper;
    }

    @RequestMapping(value = "/games", method = RequestMethod.GET)
    public PaginatedResponse<Game> allGames(
            @RequestParam(defaultValue = "1") int pageNr,
            @RequestParam(defaultValue = "10") int perPage
    ) {
        //Business logic
        //Call a method in a service class
        //Add some data to the model
        // we only return data not HTML templates
        List<Game> allGames = gameService.findAll();
        return new PaginatedResponse<Game>(200, allGames, pageNr,perPage);
    }

    @RequestMapping(value = "/games/search", method = RequestMethod.GET)
    public PaginatedResponse<Game> gameSearch(
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
        return new PaginatedResponse<Game>(200, foundGames, pageNr,perPage);
    }

    @RequestMapping(value = "/games/{gameID}/reviews", method = RequestMethod.POST)
    public ResponseEntity<String> postReview(
            @RequestHeader(value = "Authorization") String authHeader,
            @PathVariable Long gameID,
            @Valid @RequestBody Review incomingReview,
            BindingResult res
    ) {
        User user;
        try {
            //Extract current user from Auth token
            String token = authHeader.replace("Bearer ", "");
            Long userId = jwtHelper.extractUserId(token);
            user = userService.findById(userId);

            if (user == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body("You must be logged in to add a review");
            }
        }catch (JwtException e){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid or missing token");
        }

        if (res.hasErrors()) {
            String errors = res.getAllErrors().stream()
                    .map(error -> error.getDefaultMessage()).collect(Collectors.joining(", "));
            return ResponseEntity.badRequest().body(errors);
        }

        Game game = gameService.findById(gameID);

        Optional<Review> existingReview = gameService.findReview(game, user);

        if (existingReview.isPresent()) {
            return ResponseEntity.badRequest().body("Review already exists");
        }

        Review review = new Review();
        review.setRating(incomingReview.getRating());
        review.setTitle(incomingReview.getTitle());
        review.setText(incomingReview.getText());
        review.setUser(user);
        review.setGame(game);

        gameService.saveReview(review);
        return ResponseEntity.ok("review added to " + game.getTitle());
    }

    @RequestMapping(value = "/games/{gameID}/favorite", method = RequestMethod.POST)
    public ResponseEntity<?> toggleFavorite(
            @PathVariable("gameID") Long gameId,
            @RequestHeader(value = "Authorization") String authHeader
    ) {
        try {
            String token = authHeader.replace("Bearer ", "");
            Long userId = jwtHelper.extractUserId(token);
            boolean nowFavorited = userService.toggleFavorite(userId, gameId);
            return ResponseEntity.ok().body(
                    Map.of("gameId", gameId, "favorited", nowFavorited)
            );
        } catch (JwtException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Toggle favorite failed.");
        }
    }

    @RequestMapping(value = "/games/{gameID}/want", method = RequestMethod.POST)
    public ResponseEntity<?> toggleWantToPlay(
            @PathVariable("gameID") Long gameId,
            @RequestHeader(value = "Authorization") String authHeader
    ) {
        try {
            String token = authHeader.replace("Bearer ", "");
            Long userId = jwtHelper.extractUserId(token);
            boolean nowWanted = userService.toggleWantToPlay(userId, gameId);
            return ResponseEntity.ok().body(
                    Map.of("gameId", gameId, "wantToPlay", nowWanted)
            );
        } catch (JwtException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Toggle wantToPlay failed.");
        }
    }

    @RequestMapping(value = "/games/{gameID}/played", method = RequestMethod.POST)
    public ResponseEntity<?> toggleHasPlayed(
            @PathVariable("gameID") Long gameId,
            @RequestHeader(value = "Authorization") String authHeader
    ) {
        try {
            String token = authHeader.replace("Bearer ", "");
            Long userId = jwtHelper.extractUserId(token);
            boolean nowPlayed = userService.toggleHasPlayed(userId, gameId);
            return ResponseEntity.ok().body(
                    Map.of("gameId", gameId, "hasPlayed", nowPlayed)
            );
        } catch (JwtException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Toggle hasPlayed failed.");
        }
    }

}
