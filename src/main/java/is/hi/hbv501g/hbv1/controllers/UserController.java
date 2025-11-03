package is.hi.hbv501g.hbv1.controllers;

import io.jsonwebtoken.JwtException;
import is.hi.hbv501g.hbv1.extras.DTOs.NormalResponse;
import is.hi.hbv501g.hbv1.extras.entityDTOs.game.NormalGameDTO;
import is.hi.hbv501g.hbv1.extras.entityDTOs.user.MyselfUserDTO;
import is.hi.hbv501g.hbv1.extras.entityDTOs.user.NormalUserDTO;
import is.hi.hbv501g.hbv1.extras.entityDTOs.user.ReferencedUserDTO;
import is.hi.hbv501g.hbv1.extras.helpers.CloudinaryService;
import is.hi.hbv501g.hbv1.extras.helpers.JWTHelper;
import is.hi.hbv501g.hbv1.extras.DTOs.PaginatedResponse;
import is.hi.hbv501g.hbv1.extras.DTOs.UserToUpdate;
import is.hi.hbv501g.hbv1.extras.helpers.SortHelper;
import is.hi.hbv501g.hbv1.persistence.entities.Game;
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
     * a get method that allows the user to see all users in the system
     *
     * @param pageNr  which page to show [default = 1]
     * @param perPage how many genres per page [default = 10]
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
        return new PaginatedResponse<NormalUserDTO>(HttpStatus.OK.value(), allGameDTOs, pageNr, perPage);
    }

    /**
     * a method that allows the user to delete the account they are currently logged in to
     *
     * @param authHeader the header where the session token is stored
     *
     * @return a response entity with a status code and a message that will tell the user how the request went
     */
    @RequestMapping(value = "/users", method = RequestMethod.DELETE)
    public NormalResponse<Void> deleteLoggedInUser(
            @RequestHeader(value = "Authorization") String authHeader
    ) {
        User user;
        try {
            //Extract current user from Auth token
            String token = authHeader.replace("Bearer ", "");
            Long userId = jwtHelper.extractUserId(token);
            user = userService.findById(userId);

            if (user == null) {
                return new NormalResponse<>(HttpStatus.UNAUTHORIZED.value(), "You must be logged in to delete your account");
            }
        } catch (JwtException e) {
            return new NormalResponse<>(HttpStatus.UNAUTHORIZED.value(), "Invalid or missing token");
        }

        userService.delete(user);
        return new NormalResponse<>(HttpStatus.OK.value(), "Your account has been successfully deleted");
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
    public NormalResponse<NormalUserDTO> changeLoggedInUser(
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
                return new NormalResponse<>(HttpStatus.UNAUTHORIZED.value(), "You must be logged in to change your account");
            }
        } catch (JwtException e) {
            return new NormalResponse<>(HttpStatus.UNAUTHORIZED.value(), "Invalid or missing token");
        }

        if (res.hasErrors()) {
            String errors = res.getAllErrors().stream()
                    .map(error -> error.getDefaultMessage()).collect(Collectors.joining(", "));
            return new NormalResponse<>(HttpStatus.BAD_REQUEST.value(), errors);
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
        return new NormalResponse<>(HttpStatus.OK.value(), "Account information changed", new NormalUserDTO(savedUser));
    }

    /**
     * gets the info of a specific user in the system
     *
     * @param userId the id of  the user
     * @return response entity with either the users info or an error message
     */
    @GetMapping("/users/id/{userId}")
    public NormalResponse<NormalUserDTO> getPublicProfileById(@PathVariable("userId") Long userId) {
        try {
            NormalUserDTO user = userService.getPublicProfileById(userId);
            return new NormalResponse<>(HttpStatus.OK.value(), "Found user", user);
        } catch (IllegalArgumentException ex) {
            return new NormalResponse<>(HttpStatus.NOT_FOUND.value(), "No user found with this user id.");
        }
    }

    /**
     * a method that allows the user to see their own profile
     *
     * @param authHeader the header where the session token is stored
     * @return a response entity with the info of the user that is logged in or an error message
     */
    @GetMapping("/users/me")
    public NormalResponse<MyselfUserDTO> getOwnProfile(
            @RequestHeader("Authorization") String authHeader
    ) {
        try {
            String token = authHeader.replace("Bearer ", "");
            Long userId = jwtHelper.extractUserId(token);
            User me = userService.findById(userId);
            if (me == null) {
                return new NormalResponse<>(HttpStatus.UNAUTHORIZED.value(), "Invalid user");
            }
            return new NormalResponse<>(HttpStatus.OK.value(), "Found profile", new MyselfUserDTO(me));
        } catch (Exception e) {
            return new NormalResponse<>(HttpStatus.UNAUTHORIZED.value(), "Invalid or missing token");
        }
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
    public NormalResponse<NormalUserDTO> followUser(
            @PathVariable("userID") Long userID,
            @RequestHeader(value = "Authorization") String authHeader
    ) {
        User user;
        try {
            user = extractUserFromHeader(authHeader, "You must be logged in to follow another user");
        } catch (JwtException e) {
            return new NormalResponse<>(HttpStatus.UNAUTHORIZED.value(), e.getMessage());
        }

        User userToFollow = userService.findById(userID);

        if (userToFollow == null) {
            return new NormalResponse<>(HttpStatus.NOT_FOUND.value(), "User to follow not found");
        }

        try {
            userService.addFollow(user, userToFollow);
            return new NormalResponse<>(HttpStatus.OK.value(), "User: [" + user.getId() + "] followed user: [" + userID + "]");
        } catch (IllegalArgumentException error) {
            return new NormalResponse<>(HttpStatus.BAD_REQUEST.value(), error.getMessage());
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
    public NormalResponse<NormalUserDTO> unfollowUser(
            @PathVariable("userID") Long userID,
            @RequestHeader(value = "Authorization") String authHeader
    ) {
        User user;
        try {
            user = extractUserFromHeader(authHeader, "You must be logged in to unfollow another user");
        } catch (JwtException e) {
            return new NormalResponse<>(HttpStatus.UNAUTHORIZED.value(), e.getMessage());
        }

        User userToUnfollow = userService.findById(userID);

        if (userToUnfollow == null) {
            return new NormalResponse<>(HttpStatus.NOT_FOUND.value(), "User to unfollow not found");
        }

        try {
            userService.removeFollow(user, userToUnfollow);
            return new NormalResponse<>(HttpStatus.OK.value(), "User: [" + user.getId() + "] unfollowed user: [" + userID + "]");
        } catch (IllegalArgumentException error) {
            return new NormalResponse<>(HttpStatus.BAD_REQUEST.value(), error.getMessage());
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
    public PaginatedResponse<NormalUserDTO> searchUsers(
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
        return new PaginatedResponse<NormalUserDTO>(HttpStatus.OK.value(), displayList, pageNr, perPage);
    }
}
