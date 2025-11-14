package is.hi.hbv501g.hbv1.controllers;

import io.jsonwebtoken.JwtException;
import is.hi.hbv501g.hbv1.extras.DTOs.*;
import is.hi.hbv501g.hbv1.extras.entityDTOs.review.ListedReviewDTO;
import is.hi.hbv501g.hbv1.extras.entityDTOs.review.NormalReviewDTO;
import is.hi.hbv501g.hbv1.extras.helpers.JWTHelper;
import is.hi.hbv501g.hbv1.extras.helpers.SortHelper;
import is.hi.hbv501g.hbv1.persistence.entities.Game;
import is.hi.hbv501g.hbv1.persistence.entities.Review;
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
public class ReviewController extends BaseController {
    private final GameService gameService;
    private final SortHelper sortHelper;
    private final ReviewService reviewService;

    @Autowired
    public ReviewController(
            GameService gameService,
            UserService userService,
            JWTHelper jwtHelper,
            SortHelper sortHelper,
            ReviewService reviewService
    ) {
        this.gameService = gameService;
        this.userService = userService;
        this.jwtHelper = jwtHelper;
        this.sortHelper = sortHelper;
        this.reviewService = reviewService;
    }


    @RequestMapping(value = "/games/{gameID}/reviews", method= RequestMethod.GET)
    public ResponseEntity<BaseResponse<ListedReviewDTO>> seeGamesReviews(
            @PathVariable("gameID") Long gameID,
            @RequestParam(defaultValue = "1") int pageNr,
            @RequestParam(defaultValue = "10") int perPage,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "false") Boolean sortReverse
    ) {
        Game game = gameService.findById(gameID);

        if (game == null) {
            return wrap(new NormalResponse<>(HttpStatus.NOT_FOUND.value(), "Game not found"));
        }

        List<Review> allReviews = game.getReviews();
        allReviews = sortHelper.sortReviews(allReviews, sortBy, sortReverse);

        List<ListedReviewDTO> allReviewDTOs = allReviews.stream()
                .map(ListedReviewDTO::new).toList();
        return wrap(new PaginatedResponse<>(HttpStatus.OK.value(), allReviewDTOs, pageNr,perPage));
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
    public ResponseEntity<BaseResponse<NormalReviewDTO>> postReview(
            @RequestHeader(value = "Authorization") String authHeader,
            @PathVariable Long gameID,
            @Valid @RequestBody ReviewToCreate incomingReview,
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
            Review newReview = reviewService.postReview(user, game, incomingReview);
            return wrap(new NormalResponse<NormalReviewDTO>(HttpStatus.CREATED.value(), "Review successfully added to game with id: " + gameID, new NormalReviewDTO(newReview)));
        } catch( IllegalArgumentException error ) {
            return wrap(new NormalResponse<>(HttpStatus.BAD_REQUEST.value(), error.getMessage()));
        }
    }


    /**
     * Handles PATCH requests on the /games/{gameID}/reviews/{reviewID}
     *
     * @param authHeader The header containing the session token
     * @param gameID the id of the game that the review is for
     * @param reviewID the id of the review we want to edit
     *
     * @return a response entity containing information on how the request went
     */
    @RequestMapping(value = "/games/{gameID}/reviews/{reviewID}", method=RequestMethod.PATCH)
    public ResponseEntity<BaseResponse<NormalReviewDTO>> updateReview(
            @RequestHeader(value = "Authorization") String authHeader,
            @PathVariable Long gameID,
            @PathVariable Long reviewID,
            @Valid @RequestBody ReviewToUpdate updateReviewData,
            BindingResult res
    ){        //check for incoming review data
        if (res.hasErrors()){
            String errors = res.getAllErrors().stream().map(error -> error.getDefaultMessage()).collect(Collectors.joining(", "));
            return wrap(new NormalResponse<>(HttpStatus.BAD_REQUEST.value(), errors));
        }

        //authenticate the user
        User user;
        try{
            user = extractUserFromHeader(authHeader, "must be logged in to change review");
        } catch (JwtException e){
            return wrap(new NormalResponse<>(HttpStatus.UNAUTHORIZED.value(), e.getMessage()));
        }

        Game game = gameService.findById(gameID);
        if (game == null) {
            return wrap(new NormalResponse<>(HttpStatus.NOT_FOUND.value(), "Game not found"));
        }

        //then update the review
        try {
            // changed to now accept ReviewToUpdate DTO
            Review updatedReview = reviewService.updateReview(user, gameID, reviewID, updateReviewData);
            return wrap(new NormalResponse<>(HttpStatus.OK.value(), "Review " + reviewID + " update successfully", new NormalReviewDTO(updatedReview)));
        } catch (SecurityException e) {
            return wrap(new NormalResponse<>(HttpStatus.FORBIDDEN.value(), e.getMessage()));
        } catch (IllegalArgumentException error) {
            return wrap(new NormalResponse<>(HttpStatus.BAD_REQUEST.value(), error.getMessage()));
        }
    }


    /**
     * Handles DELETE requests on the /games/{gameID}/reviews/{reviewID}
     *
     * @param authHeader The header containing the session token
     * @param gameID the id of the game that the review is for
     * @param reviewID the id of the review we want to delete
     *
     * @return a response entity containing information on how the request went
     */
    @RequestMapping(value = "/games/{gameID}/reviews/{reviewID}", method = RequestMethod.DELETE)
    public ResponseEntity<BaseResponse<Void>> deleteReview(
            @RequestHeader(value = "Authorization") String authHeader,
            @PathVariable Long gameID,
            @PathVariable Long reviewID
    ) {
        User user;
        try {
            user = extractUserFromHeader(authHeader, "You must be logged in to delete a review");
        } catch (JwtException e) {
            return wrap(new NormalResponse<>(HttpStatus.UNAUTHORIZED.value(), e.getMessage()));
        }

        try {
            reviewService.deleteReview(user, gameID, reviewID);
            return wrap(new NormalResponse<>(HttpStatus.OK.value(), "Review " + reviewID + " deleted successfully"));
        } catch (SecurityException e) { //if the user is not the author
            return wrap(new NormalResponse<>(HttpStatus.FORBIDDEN.value(), e.getMessage()));
        } catch (IllegalArgumentException error) { // if the review is not found
            return wrap(new NormalResponse<>(HttpStatus.NOT_FOUND.value(), "Game not found"));
        }
    }
}
