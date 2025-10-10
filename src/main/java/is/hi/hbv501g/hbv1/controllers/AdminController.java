package is.hi.hbv501g.hbv1.controllers;

import io.jsonwebtoken.JwtException;
import is.hi.hbv501g.hbv1.extras.CloudinaryService;
import is.hi.hbv501g.hbv1.extras.GameToCreate;
import is.hi.hbv501g.hbv1.extras.JWTHelper;
import is.hi.hbv501g.hbv1.persistence.entities.Game;
import is.hi.hbv501g.hbv1.persistence.entities.Genre;
import is.hi.hbv501g.hbv1.persistence.entities.Role;
import is.hi.hbv501g.hbv1.persistence.entities.User;
import is.hi.hbv501g.hbv1.services.AuthService;
import is.hi.hbv501g.hbv1.services.GameService;
import is.hi.hbv501g.hbv1.services.GenreService;
import jakarta.validation.Valid;
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

    @Autowired
    public AdminController(GameService gameService, JWTHelper jwtHelper, AuthService authService, CloudinaryService cloudinaryService, GenreService genreService) {
        this.gameService = gameService;
        this.jwtHelper = jwtHelper;
        this.authService = authService;
        this.cloudinaryService = cloudinaryService;
        this.genreService = genreService;
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
                    .map(error -> error.getDefaultMessage()).collect(Collectors.joining(","));
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

}
