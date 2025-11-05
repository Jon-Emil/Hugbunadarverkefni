package is.hi.hbv501g.hbv1.controllers;

import is.hi.hbv501g.hbv1.extras.DTOs.BaseResponse;
import is.hi.hbv501g.hbv1.extras.DTOs.NormalResponse;
import is.hi.hbv501g.hbv1.extras.DTOs.PaginatedResponse;
import is.hi.hbv501g.hbv1.extras.entityDTOs.genre.NormalGenreDTO;
import is.hi.hbv501g.hbv1.extras.helpers.SortHelper;
import is.hi.hbv501g.hbv1.persistence.entities.Genre;
import is.hi.hbv501g.hbv1.services.GameService;
import is.hi.hbv501g.hbv1.services.GenreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Comparator;
import java.util.List;

@RestController
public class GenreController extends BaseController {
    private final GenreService genreService;
    private final SortHelper sortHelper;

    @Autowired
    public GenreController(GenreService genreService, SortHelper sortHelper) {
        this.genreService = genreService;
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
    public ResponseEntity<BaseResponse<NormalGenreDTO>> allGenres(
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

        return wrap(new PaginatedResponse<>(HttpStatus.OK.value(), allGenresDTOs, pageNr, perPage));
    }

    /**
     * returns info about a specific genre
     *
     * @param genreID id of the genre we want to view
     *
     * @return a response entity containing a http code and a message about how the request went
     */
    @RequestMapping(value = "/genres/{genreID}", method = RequestMethod.GET)
    public ResponseEntity<BaseResponse<NormalGenreDTO>> genreDetails(@PathVariable Long genreID) {
        Genre genre = genreService.findById(genreID);

        if (genre == null) {
            return wrap(new NormalResponse<>(HttpStatus.NOT_FOUND.value(), "Genre not found"));
        }

        return wrap(new NormalResponse<>(HttpStatus.OK.value(), "Genre found", new NormalGenreDTO(genre)));
    }
}
