package is.hi.hbv501g.hbv1.controllers;

import is.hi.hbv501g.hbv1.extras.PaginatedResponse;
import is.hi.hbv501g.hbv1.extras.SearchCriteria;
import is.hi.hbv501g.hbv1.persistence.entities.Game;
import is.hi.hbv501g.hbv1.services.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class GameController {
    private final GameService gameService;

    @Autowired
    public GameController(GameService gameService) {
        this.gameService = gameService;
    }

    @RequestMapping(value = "/games", method = RequestMethod.GET)
    public PaginatedResponse<Game> allGames(
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

    @RequestMapping(value = "/games/search", method = RequestMethod.GET)
    public PaginatedResponse<Game> gameSearch(
            @RequestParam(defaultValue = "1") int pageNr,
            @RequestParam(defaultValue = "10") int perPage,
            @RequestParam(required = false) String title,
            @RequestParam(required = false) Float minPrice,
            @RequestParam(required = false) Float maxPrice,
            @RequestParam(required = false) String releasedAfter,
            @RequestParam(required = false) String releasedBefore,
            @RequestParam(required = false) String developer,
            @RequestParam(required = false) String publisher,
            @RequestParam(required = false) List<String> genres
            ) {
        SearchCriteria params = new SearchCriteria(
                title, minPrice, maxPrice, releasedAfter, releasedBefore, developer, publisher, genres
        );
        List<Game> foundGames = gameService.search(params);
        return new PaginatedResponse<Game>(200, foundGames, pageNr,perPage);
    }


}
