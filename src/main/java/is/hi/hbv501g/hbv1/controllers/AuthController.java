package is.hi.hbv501g.hbv1.controllers;

import is.hi.hbv501g.hbv1.extras.DTOs.Credentials;
import is.hi.hbv501g.hbv1.extras.DTOs.NormalResponse;
import is.hi.hbv501g.hbv1.extras.helpers.JWTHelper;
import is.hi.hbv501g.hbv1.persistence.entities.User;
import is.hi.hbv501g.hbv1.persistence.repositories.UserRepository;
import is.hi.hbv501g.hbv1.services.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class AuthController extends BaseController{

    private final AuthService authService;

    @Autowired
    public AuthController(
            AuthService authService,
            JWTHelper jwtHelper
    , UserRepository userRepository) {
        this.authService = authService;
        this.setJwtHelper(jwtHelper);
    }

    /**
     * a method that allows the user to log in to their account
     *
     * @param credentials the email and password of the account the user wants to log in to
     *
     * @return a response entity with a status code and a message that should tell the user how this request went
     */
    @RequestMapping(value="/login", method=RequestMethod.POST)
    public ResponseEntity<NormalResponse<String>> login(@RequestBody Credentials credentials) {
        if (credentials.getEmail() == null || credentials.getPassword() == null) {
            // incorrect password so return a 401 error
            return wrap(new NormalResponse<>(HttpStatus.UNAUTHORIZED.value(), "Invalid credentials"));
        }

        User user = authService.findByEmail(credentials.getEmail());
        if (user == null) {
            // if no user then return a 404 error
            return wrap(new NormalResponse<>(HttpStatus.NOT_FOUND.value(), "User not found"));
        }

        if (!jwtHelper.check(credentials.getPassword(), user.getPasswordHash())) {
            // incorrect password so return a 401 error
            return wrap(new NormalResponse<>(HttpStatus.UNAUTHORIZED.value(), "Invalid credentials"));
        }

        String token = jwtHelper.generateToken(user.getId());

        return wrap(new NormalResponse<>(HttpStatus.OK.value(), "Log in was successful", token));
    }

    /**
     * a method that allows the user to create a new account
     *
     * @param credentials the password and email of the new account
     *
     * @return a response entity with a status code and a message that should tell the user how this request went
     */
    @RequestMapping(value="/register", method=RequestMethod.POST)
    public ResponseEntity<NormalResponse<String>> register(@RequestBody Credentials credentials) {
        User user = authService.findByEmail(credentials.getEmail());
        if (user != null) {
            // if there already is an account with this email then return a 409 error
            // (409 is for "Conflict" and here there is a conflict because emails are supposed to be unique)
            return wrap(new NormalResponse<>(HttpStatus.CONFLICT.value(), "An account is already registered with this email"));
        }

        String hashedPassword = jwtHelper.hash(credentials.getPassword());
        User newUser = authService.save(new User(credentials.getEmail(), credentials.getEmail(), hashedPassword));
        String token = jwtHelper.generateToken(newUser.getId());

        return wrap(new NormalResponse<>(HttpStatus.CREATED.value(), "Registration was successful", token));
    }
}
