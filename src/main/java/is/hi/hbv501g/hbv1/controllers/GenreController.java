package is.hi.hbv501g.hbv1.controllers;

import is.hi.hbv501g.hbv1.services.GameService;
import is.hi.hbv501g.hbv1.services.GenreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GenreController {
    private final GenreService genreService;

    @Autowired
    public GenreController(GenreService genreService) {
        this.genreService = genreService;
    }

}
