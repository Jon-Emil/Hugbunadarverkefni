package is.hi.hbv501g.hbv1.controllers;

import is.hi.hbv501g.hbv1.extras.PaginatedResponse;
import is.hi.hbv501g.hbv1.persistence.entities.User;
import is.hi.hbv501g.hbv1.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
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


    @RequestMapping(value = "/users", method = RequestMethod.POST)
    public User addUser(
            @RequestBody User user
    ) {
        return userService.save(user);
    }
}
