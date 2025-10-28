package is.hi.hbv501g.hbv1.controllers;

import io.jsonwebtoken.JwtException;
import is.hi.hbv501g.hbv1.extras.entityDTOs.game.NormalGameDTO;
import is.hi.hbv501g.hbv1.extras.entityDTOs.user.MyselfUserDTO;
import is.hi.hbv501g.hbv1.extras.entityDTOs.user.NormalUserDTO;
import is.hi.hbv501g.hbv1.extras.helpers.CloudinaryService;
import is.hi.hbv501g.hbv1.extras.helpers.JWTHelper;
import is.hi.hbv501g.hbv1.extras.DTOs.PaginatedResponse;
import is.hi.hbv501g.hbv1.extras.DTOs.UserToUpdate;
import is.hi.hbv501g.hbv1.extras.helpers.SortHelper;
import is.hi.hbv501g.hbv1.persistence.entities.User;
import is.hi.hbv501g.hbv1.services.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class UserController {
    private final UserService userService;
    private final JWTHelper jwtHelper;
    private final CloudinaryService cloudinaryService;
    private final SortHelper sortHelper;
    

    @Autowired
    public UserController(UserService userService, JWTHelper jwtHelper, CloudinaryService cloudinaryService, SortHelper sortHelper) {
        this.userService = userService;
        this.jwtHelper = jwtHelper;
        this.cloudinaryService = cloudinaryService;
        this.sortHelper = sortHelper;
    }

    /**
     * a get method that allows the user to see all users in the system
     *
     * @param pageNr which page to show [default = 1]
     * @param perPage how many genres per page [default = 10]
     *
     * @return a paginated response showing the status code and the list of genre for the specified page nr aswell as some extra info
     */
    @RequestMapping(value = "/users", method = RequestMethod.GET)
    public PaginatedResponse<NormalUserDTO> allUsers(
            @RequestParam(defaultValue = "1") int pageNr,
            @RequestParam(defaultValue = "10") int perPage,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "false") Boolean sortReverse
    ) {
        List<User> allUsers = userService.findAll();
        allUsers = sortHelper.sortUsers(allUsers, sortBy, sortReverse);
        List<NormalUserDTO> allGameDTOs = allUsers.stream()
                .map(NormalUserDTO::new).toList();
        return new PaginatedResponse<NormalUserDTO>(200, allGameDTOs, pageNr,perPage);
    }

    /**
     * a method that allows the user to delete the account they are currently logged in to
     *
     * @param authHeader the header where the session token is stored
     *
     * @return a response entity with a status code and a message that will tell the user how the request went
     */
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

    /**
     * a patch method that allows the user to change their account information
     *
     * @param authHeader the header where the session token is stored
     * @param userInfo the data that the user wants to change
     * @param res a binding result that tells us if the userInfo meets all requirements
     * @param profilePictureFile a picture that will replace the old profile picture and can be left blank to not replace it
     *
     * @return a response entity with a status code and a message that will tell the user how the request went
     */
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

    /**
     * gets the info of a specific user in the system
     *
     * @param userId the id of  the user
     *
     * @return response entity with either the users info or an error message
     */
    @GetMapping("/users/id/{userId}")
    public ResponseEntity<?> getPublicProfileById(@PathVariable("userId") Long userId) {
        try {
            return ResponseEntity.ok(userService.getPublicProfileById(userId));
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }
    }

    /**
     * a method that allows the user to see their own profile
     *
     * @param authHeader the header where the session token is stored
     *
     * @return a response entity with the info of the user that is logged in or an error message
     */
    @GetMapping("/users/me")
    public ResponseEntity<?> getOwnProfile(
            @RequestHeader("Authorization") String authHeader
    ) {
        try {
            String token = authHeader.replace("Bearer ", "");
            Long userId = jwtHelper.extractUserId(token);
            User me = userService.findById(userId);
            if (me == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid user");
            }
            return ResponseEntity.ok(new MyselfUserDTO(me));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid or missing token");
        }
    }

    @RequestMapping(value = "/users/{userID}/follow", method = RequestMethod.POST)
    public ResponseEntity<String> followUser(

    )
}
