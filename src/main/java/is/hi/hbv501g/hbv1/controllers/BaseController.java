package is.hi.hbv501g.hbv1.controllers;

import io.jsonwebtoken.JwtException;
import is.hi.hbv501g.hbv1.extras.DTOs.NormalResponse;
import is.hi.hbv501g.hbv1.extras.DTOs.PaginatedResponse;
import is.hi.hbv501g.hbv1.extras.helpers.JWTHelper;
import is.hi.hbv501g.hbv1.persistence.entities.Role;
import is.hi.hbv501g.hbv1.persistence.entities.User;
import is.hi.hbv501g.hbv1.services.UserService;
import org.springframework.http.ResponseEntity;

public abstract class BaseController {
    protected UserService userService;
    protected JWTHelper jwtHelper;

    /**
     * a wrapper function for the NormalResponse object that creates a response entity for it with the correct status
     *
     * @param response The response object
     * @return A response entity with the correct status that contains the NormalResponse object
     * @param <T> The type of data that the response contains
     */
    protected <T> ResponseEntity<NormalResponse<T>> wrap(NormalResponse<T> response) {
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    /**
     * a wrapper function for the PaginatedResponse object that creates a response entity for it with the correct status
     *
     * @param response The response object
     * @return A response entity with the correct status that contains the PaginatedResponse object
     * @param <T> The type of data that the response contains
     */
    protected <T> ResponseEntity<PaginatedResponse<T>> wrap(PaginatedResponse<T> response) {
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    /**
     * Helper function used to easily get the User from the userID stored in the Auth header
     *
     * @param authHeader Where the token is stored
     * @param userNotFoundError What we want the error message to be if we don't find a user matching the userID
     *
     * @return User object of the userID that is stored in the header
     */
    protected User extractUserFromHeader(String authHeader, String userNotFoundError) {
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
     * Helper function used to easily get the admin user from the userID stored in the Auth header
     *
     * @param authHeader Where the token is stored
     * @param adminNotFoundError What we want the error message to be if we don't find a user matching the userID or the user is not an admin
     *
     * @return Admin User object of the userID that is stored in the header
     */
    protected User extractAdminFromHeader(String authHeader, String adminNotFoundError) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new JwtException("Missing or malformed Authorization header");
        }

        String token = authHeader.substring(7); // safer than replace()
        Long userId = jwtHelper.extractUserId(token);
        User user = userService.findById(userId);
        if (user == null || user.getRole() != Role.ADMIN) throw new JwtException(adminNotFoundError);
        return user;
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    public void setJwtHelper(JWTHelper jwtHelper) {
        this.jwtHelper = jwtHelper;
    }
}
