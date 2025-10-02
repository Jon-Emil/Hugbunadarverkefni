package is.hi.hbv501g.hbv1.controllers;

import is.hi.hbv501g.hbv1.services.GenreService;

public class GenreController {
    private final GenreService genreService;

    public GenreController(GenreService genreService) {
        this.genreService = genreService;
    }
}