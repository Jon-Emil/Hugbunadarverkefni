package is.hi.hbv501g.hbv1.controllers;

import io.jsonwebtoken.JwtException;
import is.hi.hbv501g.hbv1.extras.DTOs.NormalResponse;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class UserController extends BaseController{
    private final CloudinaryService cloudinaryService;
    private final SortHelper sortHelper;


    @Autowired
    public UserController(UserService userService, JWTHelper jwtHelper, CloudinaryService cloudinaryService, SortHelper sortHelper) {
        this.setUserService(userService);
        this.setJwtHelper(jwtHelper);
        this.cloudinaryService = cloudinaryService;
        this.sortHelper = sortHelper;
    }

    /**
     * a get method that allows the user to see all users in the system
     *
     * @param pageNr  which page to show [default = 1]
     * @param perPage how many genres per page [default = 10]
     * @return a paginated response showing the status code and the list of genre for the specified page nr aswell as some extra info
     */
    @RequestMapping(value = "/users", method = RequestMethod.GET)
    public ResponseEntity<PaginatedResponse<NormalUserDTO>> allUsers(
            @RequestParam(defaultValue = "1") int pageNr,
            @RequestParam(defaultValue = "10") int perPage,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "false") Boolean sortReverse
    ) {
        List<User> allUsers = userService.findAll();
        allUsers = sortHelper.sortUsers(allUsers, sortBy, sortReverse);
        List<NormalUserDTO> allGameDTOs = allUsers.stream()
                .map(NormalUserDTO::new).toList();
        return wrap(new PaginatedResponse<NormalUserDTO>(HttpStatus.OK.value(), allGameDTOs, pageNr, perPage));
    }

    /**
     * a method that allows the user to delete the account they are currently logged in to
     *
     * @param authHeader the header where the session token is stored
     *
     * @return a response entity with a status code and a message that will tell the user how the request went
     */
    @RequestMapping(value = "/users", method = RequestMethod.DELETE)
    public ResponseEntity<NormalResponse<Void>> deleteLoggedInUser(
            @RequestHeader(value = "Authorization") String authHeader
    ) {
        User user;
        try {
            user = extractUserFromHeader(authHeader, "You must be logged in to delete your account");
        } catch (JwtException e) {
            return wrap(new NormalResponse<>(HttpStatus.UNAUTHORIZED.value(), e.getMessage()));
        }

        userService.delete(user);
        return wrap(new NormalResponse<>(HttpStatus.OK.value(), "Your account has been successfully deleted"));
    }

    /**
     * a patch method that allows the user to change their account information
     *
     * @param authHeader         the header where the session token is stored
     * @param userInfo           the data that the user wants to change
     * @param res                a binding result that tells us if the userInfo meets all requirements
     * @param profilePictureFile a picture that will replace the old profile picture and can be left blank to not replace it
     * @return a response entity with a status code and a message that will tell the user how the request went
     */
    @RequestMapping(value = "/users", method = RequestMethod.PATCH)
    public ResponseEntity<NormalResponse<NormalUserDTO>> changeLoggedInUser(
            @RequestHeader(value = "Authorization") String authHeader,
            @Valid @RequestPart(value = "userInfo", required = false) UserToUpdate userInfo,
            BindingResult res,
            @RequestPart(value = "profilePicture", required = false) MultipartFile profilePictureFile
    ) {
        User user;
        try {
            user = extractUserFromHeader(authHeader, "You must be logged in to change your account");
        } catch (JwtException e) {
            return wrap(new NormalResponse<>(HttpStatus.UNAUTHORIZED.value(), e.getMessage()));
        }

        if (res.hasErrors()) {
            String errors = res.getAllErrors().stream()
                    .map(error -> error.getDefaultMessage()).collect(Collectors.joining(", "));
            return wrap(new NormalResponse<>(HttpStatus.BAD_REQUEST.value(), errors));
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

        User savedUser = userService.save(user);
        return wrap(new NormalResponse<>(HttpStatus.OK.value(), "Account information changed", new NormalUserDTO(savedUser)));
    }

    /**
     * gets the info of a specific user in the system
     *
     * @param userId the id of  the user
     * @return response entity with either the users info or an error message
     */
    @GetMapping("/users/{userId}")
    public ResponseEntity<NormalResponse<NormalUserDTO>> getPublicProfileById(@PathVariable("userId") Long userId) {

        User user = userService.findById(userId);

        if (user == null) {
            return wrap(new NormalResponse<>(HttpStatus.NOT_FOUND.value(), "No user found with this user id."));
        }

        return wrap(new NormalResponse<>(HttpStatus.OK.value(), "Found user", new NormalUserDTO(user)));
    }

    /**
     * a method that allows the user to see their own profile
     *
     * @param authHeader the header where the session token is stored
     * @return a response entity with the info of the user that is logged in or an error message
     */
    @GetMapping("/users/profile")
    public ResponseEntity<NormalResponse<MyselfUserDTO>> getOwnProfile(
            @RequestHeader("Authorization") String authHeader
    ) {
        User user;
        try {
            user = extractUserFromHeader(authHeader, "You must be logged in to view your profile");
        } catch (JwtException e) {
            return wrap(new NormalResponse<>(HttpStatus.UNAUTHORIZED.value(), e.getMessage()));
        }

        return wrap(new NormalResponse<>(HttpStatus.OK.value(), "Found profile", new MyselfUserDTO(user)));
    }

    /**
     * a method that allows the user to follow another user
     *
     * @param userID user to follow
     * @param authHeader the header that contains the session token
     *
     * @return a response entity with a status code and a message that will tell the user how the request went
     */
    @RequestMapping(value = "/users/{userID}/follow", method = RequestMethod.POST)
    public ResponseEntity<NormalResponse<NormalUserDTO>> followUser(
            @PathVariable("userID") Long userID,
            @RequestHeader(value = "Authorization") String authHeader
    ) {
        User user;
        try {
            user = extractUserFromHeader(authHeader, "You must be logged in to follow another user");
        } catch (JwtException e) {
            return wrap(new NormalResponse<>(HttpStatus.UNAUTHORIZED.value(), e.getMessage()));
        }

        User userToFollow = userService.findById(userID);

        if (userToFollow == null) {
            return wrap(new NormalResponse<>(HttpStatus.NOT_FOUND.value(), "User to follow not found"));
        }

        try {
            userService.addFollow(user, userToFollow);
            return wrap(new NormalResponse<>(HttpStatus.OK.value(), "User: [" + user.getId() + "] followed user: [" + userID + "]"));
        } catch (IllegalArgumentException error) {
            return wrap(new NormalResponse<>(HttpStatus.BAD_REQUEST.value(), error.getMessage()));
        }
    }

    /**
     * a method that allows the user to unfollow another user
     *
     * @param userID user to unfollow
     * @param authHeader the header that contains the session token
     *
     * @return a response entity with a status code and a message that will tell the user how the request went
     */
    @RequestMapping(value = "/users/{userID}/follow", method = RequestMethod.DELETE)
    public ResponseEntity<NormalResponse<NormalUserDTO>> unfollowUser(
            @PathVariable("userID") Long userID,
            @RequestHeader(value = "Authorization") String authHeader
    ) {
        User user;
        try {
            user = extractUserFromHeader(authHeader, "You must be logged in to unfollow another user");
        } catch (JwtException e) {
            return wrap(new NormalResponse<>(HttpStatus.UNAUTHORIZED.value(), e.getMessage()));
        }

        User userToUnfollow = userService.findById(userID);

        if (userToUnfollow == null) {
            return wrap(new NormalResponse<>(HttpStatus.NOT_FOUND.value(), "User to unfollow not found"));
        }

        try {
            userService.removeFollow(user, userToUnfollow);
            return wrap(new NormalResponse<>(HttpStatus.OK.value(), "User: [" + user.getId() + "] unfollowed user: [" + userID + "]"));
        } catch (IllegalArgumentException error) {
            return wrap(new NormalResponse<>(HttpStatus.BAD_REQUEST.value(), error.getMessage()));
        }
    }


    /**
     * Handles GET requests on /users/search
     *
     * @param pageNr Which page to show 1 is first page [default = 1]
     * @param perPage How many items per page [default = 10]
     * @param username The username that is being searched for.
     * @param sortBy What to sort the results by.
     * @param sortReverse Whether to reverse the results or not.
     *
     * @return A PaginatedResponse with a status code of 200, how many users are in total that match the search
     * and a list of all users that match the search.
     */
    @RequestMapping(value = "/users/search", method = RequestMethod.GET)
    public ResponseEntity<PaginatedResponse<NormalUserDTO>> searchUsers(
            @RequestParam(defaultValue = "1") int pageNr,
            @RequestParam(defaultValue = "10") int perPage,
            @RequestParam String username,
            @RequestParam(defaultValue = "username") String sortBy,
            @RequestParam(defaultValue = "false") Boolean sortReverse
    ){
        List<User> foundUsers = userService.findByUsernameContaining(username);
        foundUsers = sortHelper.sortUsers(foundUsers, sortBy, sortReverse);

        List<NormalUserDTO> displayList = foundUsers.stream()
                .map(NormalUserDTO::new).toList();
        return wrap(new PaginatedResponse<NormalUserDTO>(HttpStatus.OK.value(), displayList, pageNr, perPage));
    }
}
