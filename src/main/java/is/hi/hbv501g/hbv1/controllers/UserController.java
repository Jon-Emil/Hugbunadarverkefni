package is.hi.hbv501g.hbv1.controllers;

import io.jsonwebtoken.JwtException;
import is.hi.hbv501g.hbv1.extras.CloudinaryService;
import is.hi.hbv501g.hbv1.extras.JWTHelper;
import is.hi.hbv501g.hbv1.extras.PaginatedResponse;
import is.hi.hbv501g.hbv1.extras.UserToUpdate;
import is.hi.hbv501g.hbv1.persistence.entities.Game;
import is.hi.hbv501g.hbv1.persistence.entities.Review;
import is.hi.hbv501g.hbv1.persistence.entities.User;
import is.hi.hbv501g.hbv1.persistence.repositories.GameRepository;
import is.hi.hbv501g.hbv1.persistence.repositories.ReviewRepository;
import is.hi.hbv501g.hbv1.persistence.repositories.UserRepository;
import is.hi.hbv501g.hbv1.services.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@RestController
public class UserController {
    private final UserService userService;
    private final JWTHelper jwtHelper;
    private final CloudinaryService cloudinaryService;
    

    @Autowired
    public UserController(UserService userService, JWTHelper jwtHelper, CloudinaryService cloudinaryService) {
        this.userService = userService;
        this.jwtHelper = jwtHelper;
        this.cloudinaryService = cloudinaryService;
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

    @RequestMapping(value = "/users", method = RequestMethod.DELETE)
    public ResponseEntity<String> deleteLoggedInUser(
            @RequestHeader(value = "Authorization") String authHeader
    ) {
        User user;
        try {
            //Extract current user from Auth token
            String token = authHeader.replace("Bearer ", "");
            Long userId = jwtHelper.extractUserId(token);
            user = userService.findById(userId);

            if (user == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body("You must be logged in to delete your account");
            }
        }catch (JwtException e){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid or missing token");
        }

        userService.delete(user);
        return ResponseEntity.ok().body("Your account has been successfully deleted");
    }

    @RequestMapping(value = "/users", method = RequestMethod.PATCH)
    public ResponseEntity<String> changeLoggedInUser(
            @RequestHeader(value = "Authorization") String authHeader,
            @Valid @RequestPart(value = "userInfo", required = false) UserToUpdate userInfo,
            BindingResult res,
            @RequestPart(value = "profilePicture", required = false) MultipartFile profilePictureFile
    ) {
        User user;
        try {
            //Extract current user from Auth token
            String token = authHeader.replace("Bearer ", "");
            Long userId = jwtHelper.extractUserId(token);
            user = userService.findById(userId);

            if (user == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body("You must be logged in to change your account");
            }
        }catch (JwtException e){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid or missing token");
        }

        if (res.hasErrors()) {
            String errors = res.getAllErrors().stream()
                    .map(error -> error.getDefaultMessage()).collect(Collectors.joining(", "));
            return ResponseEntity.badRequest().body(errors);
        }

        if (profilePictureFile != null) {
            String cloudinaryURL = cloudinaryService.uploadUserImage(profilePictureFile);
            user.setProfilePictureURL(cloudinaryURL);
        }

        if (userInfo != null) {
            if (userInfo.getUsername() != null) {
                user.setUsername(userInfo.getUsername());
            }

            if (userInfo.getDescription() != null) {
                user.setDescription(userInfo.getDescription());
            }
        }

        userService.save(user);
        return ResponseEntity.ok().body("Account information changed");
    }

}
