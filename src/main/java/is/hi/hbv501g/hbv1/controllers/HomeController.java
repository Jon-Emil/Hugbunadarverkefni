package is.hi.hbv501g.hbv1.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

    // the home page of our project that is currently not implemented
    // will eventually show the user what methods they can call
    @RequestMapping("/")
    public String homePage(
        @RequestParam(defaultValue = "1") int pageNr,
        @RequestParam(defaultValue = "10") int perPage
    ) {
        //Business logic
        //Call a method in a service class
        //Add some data to the model
        // we only return data not HTML templates
        return "home";
    }
}
