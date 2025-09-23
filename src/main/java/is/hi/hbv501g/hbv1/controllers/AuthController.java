package is.hi.hbv501g.hbv1.controllers;

import is.hi.hbv501g.hbv1.extras.Credentials;
import is.hi.hbv501g.hbv1.extras.JWTHelper;
import is.hi.hbv501g.hbv1.persistence.entities.User;
import is.hi.hbv501g.hbv1.services.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class AuthController {
    private final AuthService authService;
    private final JWTHelper jwtHelper;

    @Autowired
    public AuthController(
            AuthService authService,
            JWTHelper jwtHelper
    ) {
        this.authService = authService;
        this.jwtHelper = jwtHelper;
    }

    @RequestMapping(value="/login", method=RequestMethod.POST)
    public ResponseEntity<?> login(@RequestBody Credentials credentials) {
        User user = authService.findByEmail(credentials.getEmail());
        if (user == null) {
            // if no user then return a 404 error
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }

        if (jwtHelper.check(credentials.getPassword(), user.getPasswordHash())) {
            // incorrect password so return a 401 error
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
        }

        String token = jwtHelper.generateToken(user.getId());

        return ResponseEntity.ok()
                .header("Authorization", "Bearer " + token)
                .body(token);
    }

    @RequestMapping(value="/register", method=RequestMethod.POST)
    public ResponseEntity<?> register(@RequestBody Credentials credentials) {
        User user = authService.findByEmail(credentials.getEmail());
        if (user != null) {
            // if there already is an account with this email then return a 409 error
            // (409 is for "Conflict" and here there is a conflict because emails are supposed to be unique)
            return ResponseEntity.status(HttpStatus.CONFLICT).body("An account is already registered with this email");
        }

        String hashedPassword = jwtHelper.hash(credentials.getPassword());
        User newUser = authService.save(new User(credentials.getEmail(), credentials.getEmail(), hashedPassword));
        String token = jwtHelper.generateToken(newUser.getId());

        return ResponseEntity.status(HttpStatus.CREATED)
                .header("Authorization", "Bearer " + token)
                .body(token);
    }
}
