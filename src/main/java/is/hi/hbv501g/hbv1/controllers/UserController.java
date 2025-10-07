package is.hi.hbv501g.hbv1.controllers;

import is.hi.hbv501g.hbv1.extras.PaginatedResponse;
import is.hi.hbv501g.hbv1.persistence.entities.Game;
import is.hi.hbv501g.hbv1.persistence.entities.Review;
import is.hi.hbv501g.hbv1.persistence.entities.User;
import is.hi.hbv501g.hbv1.persistence.repositories.GameRepository;
import is.hi.hbv501g.hbv1.persistence.repositories.ReviewRepository;
import is.hi.hbv501g.hbv1.persistence.repositories.UserRepository;
import is.hi.hbv501g.hbv1.services.UserService;

import org.apache.velocity.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class UserController {
    private final UserService userService;
    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;
    private final GameRepository gameRepository;
    

    @Autowired
    public UserController(UserService userService,UserRepository userRepository,
                          GameRepository gameRepository,
                          ReviewRepository reviewRepository) {
        this.userService = userService;
        this.userRepository = userRepository;
        this.gameRepository = gameRepository;
        this.reviewRepository = reviewRepository;
    }

    @PostMapping("/{userId}/games/{gameId}/reviews")
    public ResponseEntity<Review> postReview(
            @PathVariable Long userId,
            @PathVariable Long gameId,
            @RequestBody Review incomingReview) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + userId));

        Game game = gameRepository.findById(gameId)
                .orElseThrow(() -> new ResourceNotFoundException("Game not found: " + gameId));

        Review review = new Review();
        review.setRating(incomingReview.getRating());
        review.setTitle(incomingReview.getTitle());
        review.setText(incomingReview.getText());
        review.setUser(user);
        review.setGame(game);

        Review saved = reviewRepository.save(review);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @RequestMapping(value = "/users", method = RequestMethod.GET)
    public PaginatedResponse<User> allUsers(
            @RequestParam(defaultValue = "1") int pageNr,
            @RequestParam(defaultValue = "10") int perPage
    ) {
        //Business logic
        //Call a method in a service class
        //Add some data to the model
        // we only return data not HTML templates
        List<User> allUsers = userService.findAll();
        return new PaginatedResponse<User>(200, allUsers, pageNr,perPage);
    }


    @RequestMapping(value = "/users", method = RequestMethod.POST)
    public User addUser(
            @RequestBody User user
    ) {
        return userService.save(user);
    }


}
