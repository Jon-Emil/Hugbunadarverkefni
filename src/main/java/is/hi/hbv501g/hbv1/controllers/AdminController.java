package is.hi.hbv501g.hbv1.controllers;

import io.jsonwebtoken.JwtException;
import is.hi.hbv501g.hbv1.extras.CloudinaryService;
import is.hi.hbv501g.hbv1.extras.GameToCreate;
import is.hi.hbv501g.hbv1.extras.GameToUpdate;
import is.hi.hbv501g.hbv1.extras.JWTHelper;
import is.hi.hbv501g.hbv1.persistence.entities.Game;
import is.hi.hbv501g.hbv1.persistence.entities.Genre;
import is.hi.hbv501g.hbv1.persistence.entities.Role;
import is.hi.hbv501g.hbv1.persistence.entities.User;
import is.hi.hbv501g.hbv1.services.AuthService;
import is.hi.hbv501g.hbv1.services.GameService;
import is.hi.hbv501g.hbv1.services.GenreService;
import is.hi.hbv501g.hbv1.services.UserService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class AdminController {

    private final GameService gameService;
    private final GenreService genreService;
    private final JWTHelper jwtHelper;
    private final AuthService authService;
    private final CloudinaryService cloudinaryService;
    private final UserService userService;

    @Autowired
    public AdminController(GameService gameService, JWTHelper jwtHelper, AuthService authService, CloudinaryService cloudinaryService, GenreService genreService, UserService userService) {
        this.gameService = gameService;
        this.jwtHelper = jwtHelper;
        this.authService = authService;
        this.cloudinaryService = cloudinaryService;
        this.genreService = genreService;
        this.userService = userService;
    }

    @RequestMapping(value = "/admin/addGame", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, method = RequestMethod.POST)
    public ResponseEntity<String> addGame(
            @RequestHeader(value = "Authorization") String authHeader,
            @Valid @RequestPart("game") GameToCreate gameToCreate,
            BindingResult res,
            @RequestPart("coverImage") MultipartFile coverImageFile
            ) {

        try {
            //Extract current user from Auth token
            String token = authHeader.replace("Bearer ", "");
            Long userId = jwtHelper.extractUserId(token);
            User user = authService.findById(userId);

            //Verify user is ADMIN
            if (user == null || user.getRole() != Role.ADMIN) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body("You must be an admin to add a game");
            }
        }catch (JwtException e){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid or missing token");
        }

        //Check validation of Game details
        if (res.hasErrors()) {
            String errors = res.getAllErrors().stream()
                    .map(error -> error.getDefaultMessage()).collect(Collectors.joining(", "));
            return ResponseEntity.badRequest().body(errors);
        }


        //Need to check image type HERE
        String cloudinaryUrl = cloudinaryService.uploadGameImage(coverImageFile);


        Game game = new Game(
                gameToCreate.getTitle(),
                gameToCreate.getDescription(),
                gameToCreate.getReleaseDate(),
                gameToCreate.getPrice(),
                cloudinaryUrl,
                gameToCreate.getPublisher(),
                gameToCreate.getDeveloper()
                );
        gameService.add(game, gameToCreate.getGenreIds());

        return ResponseEntity.ok("Game added!");
    }

    @RequestMapping(value = "/admin/addGenre", method = RequestMethod.POST)
    public ResponseEntity<String> addGenre(@RequestBody Genre genre) {

        //NEEDS ADMIN VERIFICATION
        genreService.save(genre);
        return ResponseEntity.ok("Genre added!");
    }

    @RequestMapping(value = "admin/deleteUser/{userID}", method = RequestMethod.DELETE)
    public ResponseEntity<String> deleteLoggedInUser(
            @RequestHeader(value = "Authorization") String authHeader,
            @PathVariable Long userID
    ) {
        User admin;
        try {
            //Extract current user from Auth token
            String token = authHeader.replace("Bearer ", "");
            Long userId = jwtHelper.extractUserId(token);
            admin = userService.findById(userId);

            if (admin == null || admin.getRole() != Role.ADMIN) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body("You must be an admin to delete an account");
            }
        }catch (JwtException e){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid or missing token");
        }

        User user = userService.findById(userID);

        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }

        userService.delete(user);
        return ResponseEntity.ok().body("Successfully deleted user with id: " + user.getId());
    }

    @RequestMapping(value = "/admin/updateGame/{gameID}")
    public ResponseEntity<String> updateGame(
            @RequestHeader(value = "Authorization") String authHeader,
            @PathVariable Long gameID,
            @Valid @RequestPart(value = "gameInfo", required = false) GameToUpdate gameInfo,
            BindingResult res,
            @RequestPart(value = "coverImage", required = false) MultipartFile coverImageFile
    ) {
        User admin;
        try {
            //Extract current user from Auth token
            String token = authHeader.replace("Bearer ", "");
            Long userId = jwtHelper.extractUserId(token);
            admin = userService.findById(userId);

            if (admin == null || admin.getRole() != Role.ADMIN) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body("You must be an admin to delete an account");
            }
        }catch (JwtException e){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid or missing token");
        }

        Game game = gameService.findById(gameID);

        if (game == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Game not found");
        }

        if (res.hasErrors()) {
            String errors = res.getAllErrors().stream()
                    .map(error -> error.getDefaultMessage()).collect(Collectors.joining(", "));
            return ResponseEntity.badRequest().body(errors);
        }

        if (coverImageFile != null) {
            String cloudinaryURL = cloudinaryService.uploadGameImage(coverImageFile);
            game.setCoverimage(cloudinaryURL);
        }

        if (gameInfo != null) {
             if (gameInfo.getTitle() != null) {
                 game.setTitle(gameInfo.getTitle());
             }
             if (gameInfo.getDescription() != null) {
                 game.setDescription(gameInfo.getDescription());
             }
             if (gameInfo.getReleaseDate() != null) {
                 game.setReleaseDate(gameInfo.getReleaseDate());
             }
             if (gameInfo.getPrice() != null) {
                 game.setPrice(gameInfo.getPrice());
             }
             if (gameInfo.getDeveloper() != null) {
                 game.setDeveloper(gameInfo.getDeveloper());

             }
             if (gameInfo.getPublisher() != null) {
                 game.setPublisher(gameInfo.getPublisher());

             }
             if (gameInfo.getGenreIds() != null) {
                 List<Genre> genres = genreService.getGenresByIds(gameInfo.getGenreIds());
                 game.setGenres(genres);
            }
        }

        gameService.save(game);
        return ResponseEntity.ok().body("Successfully updated game with id: " + gameID);
    }

    @RequestMapping(value = "/admin/deleteGame/{gameID}", method = RequestMethod.DELETE)
    public ResponseEntity<String> deleteGame(
            @RequestHeader(value = "Authorization") String authHeader,
            @PathVariable Long gameID
    ) {
        User admin;
        try {
            //Extract current user from Auth token
            String token = authHeader.replace("Bearer ", "");
            Long userId = jwtHelper.extractUserId(token);
            admin = userService.findById(userId);

            if (admin == null || admin.getRole() != Role.ADMIN) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body("You must be an admin to delete an account");
            }
        }catch (JwtException e){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid or missing token");
        }

        Game game = gameService.findById(gameID);

        if (game == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Game not found");
        }

        gameService.delete(game);
        return ResponseEntity.ok().body("Successfully deleted game with id: " + gameID);
    }
}
