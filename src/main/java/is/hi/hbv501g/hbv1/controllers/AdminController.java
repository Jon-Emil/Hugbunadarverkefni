package is.hi.hbv501g.hbv1.controllers;

import io.jsonwebtoken.JwtException;
import is.hi.hbv501g.hbv1.extras.DTOs.GenreToCreate;
import is.hi.hbv501g.hbv1.extras.DTOs.GenreToUpdate;
import is.hi.hbv501g.hbv1.extras.helpers.CloudinaryService;
import is.hi.hbv501g.hbv1.extras.DTOs.GameToCreate;
import is.hi.hbv501g.hbv1.extras.DTOs.GameToUpdate;
import is.hi.hbv501g.hbv1.extras.helpers.JWTHelper;
import is.hi.hbv501g.hbv1.persistence.entities.Game;
import is.hi.hbv501g.hbv1.persistence.entities.Genre;
import is.hi.hbv501g.hbv1.persistence.entities.Role;
import is.hi.hbv501g.hbv1.persistence.entities.User;
import is.hi.hbv501g.hbv1.services.AuthService;
import is.hi.hbv501g.hbv1.services.GameService;
import is.hi.hbv501g.hbv1.services.GenreService;
import is.hi.hbv501g.hbv1.services.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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

    /**
     * a Post method that allows the user to add a game to the system as long as they are logged in to an admin account
     *
     * @param authHeader The header where the session token is stored
     * @param gameToCreate Information about the game that is being created
     * @param res the BindingResults of the gameToCreate which just tell us if the gameToCreate conforms to the rules it has
     * @param coverImageFile The image file that will become the cover for this new game
     *
     * @return a response entity with a status code and a message that should tell the user how this request went
     */
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

    /**
     * A delete method that allows an admin to remove an account from the system
     *
     * @param authHeader the header where the session token is stored
     * @param userID the id of the user that we want to delete from our system
     *
     * @return a response entity with a status code and a message that should tell the user how this request went
     */
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

    /**
     * A patch method that allows an admin to change the data of a specific game
     *
     * @param authHeader the header where the session token is stored
     * @param gameID id of the game we want to change
     * @param gameInfo the data that we want to change
     * @param res a binding result that we can use to verify if the gameInfo is valid
     * @param coverImageFile the image that will replace the old coverImage can be left empty for no change
     *
     * @return a response entity with a status code and a message that should tell the user how this request went
     */
    @RequestMapping(value = "/admin/updateGame/{gameID}", method = RequestMethod.PATCH)
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

    /**
     * a delete method that allows an admin to delete a specific game from the system
     *
     * @param authHeader the header where the session token is stored
     * @param gameID the id of the game we want to delete
     *
     * @return a response entity with a status code and a message that should tell the user how this request went
     */
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
                        .body("You must be an admin to delete a game");
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

    /**
     * A method that allows the admin to add a genre to the system
     *
     * @param authHeader the header where the session token is stored
     * @param genre genre information to be saved
     *
     * @return a response entity with a status code and a message that should tell the user how this request went
     */
    @RequestMapping(value = "/admin/addGenre", method = RequestMethod.POST)
    public ResponseEntity<String> addGenre(
            @RequestHeader(value = "Authorization") String authHeader,
            @Valid @RequestBody GenreToCreate genre,
            BindingResult res
    ) {
        User admin;
        try {
            String token = authHeader.replace("Bearer ", "");
            Long userId = jwtHelper.extractUserId(token);
            admin = userService.findById(userId);

            if (admin == null || admin.getRole() != Role.ADMIN) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body("You must be an admin to add a genre");
            }
        }catch (JwtException e){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid or missing token");
        }

        if (res.hasErrors()) {
            String errors = res.getAllErrors().stream()
                    .map(error -> error.getDefaultMessage()).collect(Collectors.joining(", "));
            return ResponseEntity.badRequest().body(errors);
        }

        if (genreService.findByTitle(genre.getTitle()) != null) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Genre already exists");
        }


        Genre newGenre = new Genre(genre.getTitle(), genre.getDescription());

        genreService.save(newGenre);
        return ResponseEntity.ok("Genre added!");
    }

    /**
     * A method that allows the admin to update a genre in the system
     *
     * @param authHeader the header where the session token is stored
     * @param genre genre information to be updated
     *
     * @return a response entity with a status code and a message that should tell the user how this request went
     */
    @RequestMapping(value = "/admin/updateGenre/{genreID}", method = RequestMethod.PATCH)
    public ResponseEntity<String> updateGenre(
            @RequestHeader(value = "Authorization") String authHeader,
            @Valid @RequestBody GenreToUpdate genre,
            BindingResult res,
            @PathVariable Long genreID
    ) {
        User admin;
        try {
            String token = authHeader.replace("Bearer ", "");
            Long userId = jwtHelper.extractUserId(token);
            admin = userService.findById(userId);

            if (admin == null || admin.getRole() != Role.ADMIN) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body("You must be an admin to update a genre");
            }
        }catch (JwtException e){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid or missing token");
        }

        Genre existingGenre = genreService.findById(genreID);

        if (existingGenre == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Genre not found");
        }

        if (res.hasErrors()) {
            String errors = res.getAllErrors().stream()
                    .map(error -> error.getDefaultMessage()).collect(Collectors.joining(", "));
            return ResponseEntity.badRequest().body(errors);
        }

        if(genre.getTitle() != null){
            existingGenre.setTitle(genre.getTitle());
        }
        if(genre.getDescription() != null){
            existingGenre.setDescription(genre.getDescription());
        }

        genreService.save(existingGenre);
        return ResponseEntity.ok("Genre successfully updated with id : " + genreID);
    }

    /**
     * a method that allows an admin to delete a genre from the system
     *
     * @param authHeader the header where the session token is stored
     * @param genreID the id of the genre we want to delete
     *
     * @return a response entity with a status code and a message that should tell the user how this request went
     */
    @RequestMapping(value = "admin/deleteGenre/{genreID}", method = RequestMethod.DELETE)
    public ResponseEntity<String> deleteGenre(
            @RequestHeader(value = "Authorization") String authHeader,
            @PathVariable Long genreID
    ) {
        User admin;
        try {
            String token = authHeader.replace("Bearer ", "");
            Long userId = jwtHelper.extractUserId(token);
            admin = userService.findById(userId);

            if (admin == null || admin.getRole() != Role.ADMIN) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body("You must be an admin to delete a genre");
            }
        }catch (JwtException e){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid or missing token");
        }

        Genre genre = genreService.findById(genreID);

        if (genre == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Genre does not exist");
        }

        genreService.delete(genre);
        return ResponseEntity.ok().body("Successfully deleted genre with id: " + genreID);
    }
}
