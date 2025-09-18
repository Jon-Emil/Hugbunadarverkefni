package is.hi.hbv501g.hbv1.controllers;

import is.hi.hbv501g.hbv1.api.PaginatedResponse;
import is.hi.hbv501g.hbv1.persistence.entities.Game;
import is.hi.hbv501g.hbv1.services.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class HomeController {
    private GameService gameService;

    @Autowired
    public HomeController(GameService gameService) {
        this.gameService = gameService;
    }

    @RequestMapping("/games")
    public PaginatedResponse<Game> homePage(
        @RequestParam(defaultValue = "1") int pageNr,
        @RequestParam(defaultValue = "10") int perPage
    ) {
        //Business logic
        //Call a method in a service class
        //Add some data to the model
        // we only return data not HTML templates
        List<Game> allGames = gameService.findAll();
        return new PaginatedResponse<Game>(200, allGames, pageNr,perPage);
    }


}
