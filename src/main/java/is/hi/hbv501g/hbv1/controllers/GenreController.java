package is.hi.hbv501g.hbv1.controllers;

import is.hi.hbv501g.hbv1.extras.DTOs.PaginatedResponse;
import is.hi.hbv501g.hbv1.extras.entityDTOs.genre.NormalGenreDTO;
import is.hi.hbv501g.hbv1.extras.helpers.SortHelper;
import is.hi.hbv501g.hbv1.persistence.entities.Genre;
import is.hi.hbv501g.hbv1.services.GameService;
import is.hi.hbv501g.hbv1.services.GenreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Comparator;
import java.util.List;

@RestController
public class GenreController {
    private final GenreService genreService;
    private final GameService gameService;
    private final SortHelper sortHelper;

    @Autowired
    public GenreController(GenreService genreService, GameService gameService, SortHelper sortHelper) {
        this.genreService = genreService;
        this.gameService = gameService;
        this.sortHelper = sortHelper;
    }

    /**
     * a get method that allows the user to see all genres in the system
     *
     * @param pageNr which page to show [default = 1]
     * @param perPage how many genres per page [default = 10]
     *
     * @return a paginated response showing the status code and the list of genre for the specified page nr aswell as some extra info
     */
    @RequestMapping(value = "/genres", method = RequestMethod.GET)
    public PaginatedResponse<NormalGenreDTO> allGenres(
            @RequestParam(defaultValue = "1") int pageNr,
            @RequestParam(defaultValue = "10") int perPage,
            @RequestParam(defaultValue = "title") String sortBy,
            @RequestParam(defaultValue = "false") boolean sortReverse
    ) {
        List<Genre> allGenres = genreService.findAll();

        allGenres = sortHelper.sortGenres(allGenres, sortBy, sortReverse);

        List<NormalGenreDTO> allGenresDTOs = allGenres.stream()
                .map(NormalGenreDTO::new)
                .toList();

        return new PaginatedResponse<>(HttpStatus.OK.value(), allGenresDTOs, pageNr, perPage);
    }

    // not finished
    @RequestMapping(value = "/genres/{genreID}", method = RequestMethod.GET)
    public NormalGenreDTO genreDetails(@PathVariable Long genreID) {
        Genre genre = genreService.findById(genreID);
        return new NormalGenreDTO(genre);
    }
}
