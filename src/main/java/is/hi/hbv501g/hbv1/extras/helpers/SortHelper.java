package is.hi.hbv501g.hbv1.extras.helpers;

import is.hi.hbv501g.hbv1.persistence.entities.Game;
import is.hi.hbv501g.hbv1.persistence.entities.Genre;
import is.hi.hbv501g.hbv1.persistence.entities.Review;
import is.hi.hbv501g.hbv1.persistence.entities.User;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;

@Component
public class SortHelper {

    public List<Game> sortGames(List<Game> games, String sortBy, boolean reversed) {
        if (games == null) {
            return null;
        }
        if (sortBy == null) {
            sortBy = "id";
        }

        Comparator<Game> comparator = null;

        switch(sortBy.trim()) {
            case "title":
                comparator = Comparator.comparing(
                        Game::getTitle,
                        Comparator.nullsLast(String::compareToIgnoreCase)
                );
                break;
            case "releaseDate":
                comparator = Comparator.comparing(
                        Game::getReleaseDate,
                        Comparator.nullsLast(String::compareToIgnoreCase)
                );
                break;
            case "price":
                comparator = Comparator.comparingDouble(Game::getPrice);
                break;
            case "developer":
                comparator = Comparator.comparing(
                        Game::getDeveloper,
                        Comparator.nullsLast(String::compareToIgnoreCase)
                );
                break;
            case "publisher":
                comparator = Comparator.comparing(
                        Game::getPublisher,
                        Comparator.nullsLast(String::compareToIgnoreCase)
                );
                break;
            case "reviewAmount":
                comparator = Comparator.comparingInt(
                        (Game g) -> g.getReviews() == null ? 0 : g.getReviews().size()
                );
                reversed = !reversed; // we want the default to be from highest to lowest so we just flip the reversed value
                break;
            case "favoritesAmount":
                comparator = Comparator.comparingInt(
                        (Game g) -> g.getFavoriteOf() == null ? 0 : g.getFavoriteOf().size()
                );
                reversed = !reversed; // we want the default to be from highest to lowest so we just flip the reversed value
                break;
            case "wantToPlayAmount":
                comparator = Comparator.comparingInt(
                        (Game g) -> g.getWantToPlay() == null ? 0 : g.getWantToPlay().size()
                );
                reversed = !reversed; // we want the default to be from highest to lowest so we just flip the reversed value
                break;
            case "havePlayedAmount":
                comparator = Comparator.comparingInt(
                        (Game g) -> g.getHavePlayed() == null ? 0 : g.getHavePlayed().size()
                );
                reversed = !reversed; // we want the default to be from highest to lowest so we just flip the reversed value
                break;
            case "averageRating":
                comparator = Comparator.comparing(
                        Game::getAverageRating,
                        Comparator.nullsLast(Comparator.reverseOrder())
                );
                break;
            default:
                comparator = Comparator.comparingLong(Game::getId);
        }

        if (reversed) {
            comparator = comparator.reversed();
        }

        games.sort(comparator);
        return games;
    }

    public List<Genre> sortGenres(List<Genre> genres, String sortBy, Boolean reversed) {
        if (genres == null) {
            return null;
        }
        if (sortBy == null) {
            sortBy = "id";
        }

        Comparator<Genre> comparator = null;

        switch(sortBy.trim()) {
            case "title":
                comparator = Comparator.comparing(
                        Genre::getTitle,
                        Comparator.nullsLast(String::compareToIgnoreCase)
                );
                break;
            case "gameAmount":
                comparator = Comparator.comparingInt(
                        (Genre g) -> g.getGames() == null ? 0 : g.getGames().size()
                );
                reversed = !reversed; // we want the default to be from highest to lowest so we just flip the reversed value
                break;
            default:
                comparator = Comparator.comparingLong(Genre::getId);
        }

        if (reversed) {
            comparator = comparator.reversed();
        }

        genres.sort(comparator);
        return genres;
    }

    public List<Review> sortReviews(List<Review> reviews, String sortBy, Boolean reversed) {
        if (reviews == null) {
            return null;
        }
        if (sortBy == null) {
            sortBy = "id";
        }

        Comparator<Review> comparator = null;

        switch(sortBy.trim()) {
            case "title":
                comparator = Comparator.comparing(
                        Review::getTitle,
                        Comparator.nullsLast(String::compareToIgnoreCase)
                );
                break;
            case "rating":
                comparator = Comparator.comparingInt(Review::getRating);
                reversed = !reversed; // we want the default to be from highest to lowest so we just flip the reversed value
                break;
            case "username":
                comparator = Comparator.comparing(
                        (Review r) -> r.getUser() == null ? null : r.getUser().getUsername(),
                        Comparator.nullsLast(String::compareToIgnoreCase)
                );
                break;
            case "gameTitle":
                comparator = Comparator.comparing(
                        (Review r) -> r.getGame() == null ? null : r.getGame().getTitle(),
                        Comparator.nullsLast(String::compareToIgnoreCase)
                );
                break;
            default:
                comparator = Comparator.comparingLong(Review::getId);
        }

        if (reversed) {
            comparator = comparator.reversed();
        }

        reviews.sort(comparator);
        return reviews;
    }

    public List<User> sortUsers(List<User> users, String sortBy, Boolean reversed) {
        if (users == null) {
            return null;
        }
        if (sortBy == null) {
            sortBy = "id";
        }

        Comparator<User> comparator = null;

        switch(sortBy.trim()) {
            case "username":
                comparator = Comparator.comparing(
                        User::getUsername,
                        Comparator.nullsLast(String::compareToIgnoreCase)
                );
                break;
            case "followers":
                comparator = Comparator.comparingInt(
                        (User u) -> u.getFollowedBy() == null ? 0 : u.getFollowedBy().size()
                );
                reversed = !reversed; // we want the default to be from highest to lowest so we just flip the reversed value
                break;
            case "following":
                comparator = Comparator.comparingInt(
                        (User u) -> u.getFollows() == null ? 0 : u.getFollows().size()
                );
                reversed = !reversed; // we want the default to be from highest to lowest so we just flip the reversed value
                break;
            case "reviewAmount":
                comparator = Comparator.comparingInt(
                        (User u) -> u.getReviews() == null ? 0 : u.getReviews().size()
                );
                reversed = !reversed; // we want the default to be from highest to lowest so we just flip the reversed value
                break;
            case "favoriteAmount":
                comparator = Comparator.comparingInt(
                        (User u) -> u.getFavorites() == null ? 0 : u.getFavorites().size()
                );
                reversed = !reversed; // we want the default to be from highest to lowest so we just flip the reversed value
                break;
            case "wantToPlayAmount":
                comparator = Comparator.comparingInt(
                        (User u) -> u.getWantsToPlay() == null ? 0 : u.getWantsToPlay().size()
                );
                reversed = !reversed; // we want the default to be from highest to lowest so we just flip the reversed value
                break;
            case "hasPlayedAmount":
                comparator = Comparator.comparingInt(
                        (User u) -> u.getHasPlayed() == null ? 0 : u.getHasPlayed().size()
                );
                reversed = !reversed; // we want the default to be from highest to lowest so we just flip the reversed value
                break;
            default:
                comparator = Comparator.comparingLong(User::getId);
        }

        if (reversed) {
            comparator = comparator.reversed();
        }

        users.sort(comparator);
        return users;
    }
}
