package is.hi.hbv501g.hbv1.controllers;

import is.hi.hbv501g.hbv1.extras.DTOs.PaginatedResponse;
import is.hi.hbv501g.hbv1.extras.entityDTOs.genre.NormalGenreDTO;
import is.hi.hbv501g.hbv1.persistence.entities.Genre;
import is.hi.hbv501g.hbv1.services.GameService;
import is.hi.hbv501g.hbv1.services.GenreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Comparator;
import java.util.List;

@RestController
public class GenreController {
    private final GenreService genreService;
    private final GameService gameService;

    @Autowired
    public GenreController(GenreService genreService, GameService gameService) {
        this.genreService = genreService;
        this.gameService = gameService;
    }

    @RequestMapping(value = "/genres", method = RequestMethod.GET)
    public PaginatedResponse<NormalGenreDTO> allGenres(
            @RequestParam(defaultValue = "1") int pageNr,
            @RequestParam(defaultValue = "10") int perPage
    ) {
        List<Genre> allGenres = genreService.findAll();

        allGenres.sort(Comparator.comparing(Genre::getTitle, Comparator.nullsLast(String.CASE_INSENSITIVE_ORDER)));

        List<NormalGenreDTO> allGenresDTOs = allGenres.stream()
                .map(NormalGenreDTO::new)
                .toList();

        return new PaginatedResponse<>(200, allGenresDTOs, pageNr, perPage);
    }

    @RequestMapping(value = "/genres/{genreID}", method = RequestMethod.GET)
    public NormalGenreDTO genreDetails(@PathVariable Long genreID) {
        Genre genre = genreService.findById(genreID);
        return new NormalGenreDTO(genre);
    }
}
