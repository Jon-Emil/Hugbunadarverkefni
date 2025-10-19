package is.hi.hbv501g.hbv1.services.implementations;

import is.hi.hbv501g.hbv1.persistence.entities.Genre;
import is.hi.hbv501g.hbv1.persistence.repositories.GenreRepository;
import is.hi.hbv501g.hbv1.services.GenreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GenreServiceImplementation implements GenreService {

    private GenreRepository genreRepository;

    @Autowired
    public GenreServiceImplementation(GenreRepository genreRepository) {
        this.genreRepository = genreRepository;
    }

    /**
     * finds a genre by its id
     *
     * @param id the id of the genre we want to find
     *
     * @return the found genre or null if no genre was found
     */
    @Override
    public Genre findById(Long id) {
        return genreRepository.findById(id).orElse(null);
    }

    /**
     * finds all genres in the system
     *
     * @return a list of all genres in the system
     */
    @Override
    public List<Genre> findAll() {
        return genreRepository.findAll();
    }

    /**
     * finds a genre by its title
     *
     * @param title the title of the genre we want to find
     *
     * @return the first found genre
     */
    @Override
    public Genre findByTitle(String title) {
        //Should fix get first to return list if many genres have same title
        return genreRepository.findByTitle(title).getFirst();
    }

    /**
     * saves a genre to the system
     *
     * @param genre the genre to be saved
     *
     * @return the genre that was saved
     */
    @Override
    public Genre save(Genre genre) {
        return genreRepository.save(genre);
    }

    /**
     * deletes a genre from the system
     *
     * @param genre the genre that will be deleted
     */
    @Override
    public void delete(Genre genre) {
        genreRepository.delete(genre);
    }

    /**
     * gets all the genres that match the ids provided
     *
     * @param genreIds a list of ids for the genres we want to find
     *
     * @return a list of genres that was found
     */
    @Override
    public List<Genre> getGenresByIds(List<Long> genreIds) {
        return genreRepository.findAllById(genreIds);
    }
}
