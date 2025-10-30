package is.hi.hbv501g.hbv1.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
public class HomeController {

    /**
     * Shows the user all available endpoints and info about them
     *
     * @return Response entity containing a JSON object
     */
    @RequestMapping("/")
    public ResponseEntity<Map<String, Object>> homePage()
    {
        Map<String, Object> response = Map.of(
                "description", "Welcome to our game catalog API!",
                "status", "200",
                "endpoints", List.of(
                    "game endpoints", List.of(
                            Map.of(
                                    "path", "/games",
                                    "method", "GET",
                                    "description", "Displays all games in the system",
                                    "Tags", "[Sortable], [Paginated]"
                            ),
                            Map.of(
                                    "path", "/games/{gameID}",
                                    "method", "GET",
                                    "description", "Gets info about the specific game",
                                    "Tags", "[Dynamic]"
                            ),
                            Map.of(
                                    "path", "/games/search",
                                    "method", "GET",
                                    "description", "Search for games by title, price, release date, developer, publisher, or genres",
                                    "Tags", "[Sortable], [Paginated], [Extra Parameters]"
                            ),
                            Map.of(
                                    "path", "/games/{gameID}/reviews",
                                    "method", "POST",
                                    "description", "Post a new review for a specific game",
                                    "Tags", "[Dynamic], [Log In Required], [Requires Valid Request Body]"
                            ),
                            Map.of(
                                    "path", "/games/{gameID}/favorite",
                                    "method", "POST",
                                    "description", "Add a game to the authenticated user’s favorites list",
                                    "Tags", "[Dynamic], [Log In Required]"
                            ),
                            Map.of(
                                    "path", "/games/{gameID}/wants",
                                    "method", "POST",
                                    "description", "Add a game to the authenticated user’s 'want to play' list",
                                    "Tags", "[Dynamic], [Log In Required]"
                            ),
                            Map.of(
                                    "path", "/games/{gameID}/played",
                                    "method", "POST",
                                    "description", "Add a game to the authenticated user’s 'has played' list",
                                    "Tags", "[Dynamic], [Log In Required]"
                            ),
                            Map.of(
                                    "path", "/games/{gameID}/favorite",
                                    "method", "DELETE",
                                    "description", "Remove a game from the authenticated user’s favorites list",
                                    "Tags", "[Dynamic], [Log In Required]"
                            ),
                            Map.of(
                                    "path", "/games/{gameID}/wants",
                                    "method", "DELETE",
                                    "description", "Remove a game from the authenticated user’s 'want to play' list",
                                    "Tags", "[Dynamic], [Log In Required]"
                            ),
                            Map.of(
                                    "path", "/games/{gameID}/played",
                                    "method", "DELETE",
                                    "description", "Remove a game from the authenticated user’s 'has played' list",
                                    "Tags", "[Dynamic], [Log In Required]"
                            ),
                            Map.of(
                                    "path", "/games/genre/{genreId}",
                                    "method", "GET",
                                    "description", "Get all games belonging to a specific genre",
                                    "Tags", "[Sortable], [Paginated], [Dynamic]"
                            )
                    ),
                    "user endpoints", List.of(
                            Map.of(
                                    "path", "/users",
                                    "method", "GET",
                                    "description", "Displays all users in the system",
                                    "Tags", "[Sortable], [Paginated]"
                            ),
                            Map.of(
                                    "path", "/users",
                                    "method", "DELETE",
                                    "description", "Deletes the account of the authenticated user",
                                    "Tags", "[Log In Required]"
                            ),
                            Map.of(
                                    "path", "/users",
                                    "method", "PATCH",
                                    "description", "Updates account info of the authenticated user",
                                    "Tags", "[Log In Required], [Requires Valid Request Body]"
                            ),
                            Map.of(
                                    "path", "/users/id/{userId}",
                                    "method", "GET",
                                    "description", "Gets the public profile of a specific user",
                                    "Tags", "[Dynamic]"
                            ),
                            Map.of(
                                    "path", "/users/me",
                                    "method", "GET",
                                    "description", "Gets the profile of the currently authenticated user",
                                    "Tags", "[Log In Required]"
                            ),
                            Map.of(
                                    "path", "/users/{userID}/follow",
                                    "method", "POST",
                                    "description", "Follow another user (requires authentication)",
                                    "Tags", "[Dynamic], [Log In Required]"
                            ),
                            Map.of(
                                    "path", "/users/{userID}/follow",
                                    "method", "DELETE",
                                    "description", "Unfollow another user (requires authentication)",
                                    "Tags", "[Dynamic], [Log In Required]"
                            ),
                            Map.of(
                                    "path", "/users/search",
                                    "method", "GET",
                                    "description", "Find a user by their username",
                                    "Tags", "[Sortable], [Paginated], [Extra Parameters]"
                            )
                    ),
                    "admin endpoints", List.of(
                            Map.of(
                                    "path", "/admin/addGame",
                                    "method", "POST",
                                    "description", "Allows an admin to add a new game to the system",
                                    "Tags", "[Log In Required], [Admin Only], [Requires Valid Request Body]"
                            ),
                            Map.of(
                                    "path", "/admin/updateGame/{gameID}",
                                    "method", "PATCH",
                                    "description", "Allows an admin to update information about a specific game",
                                    "Tags", "[Dynamic], [Log In Required], [Admin Only], [Requires Valid Request Body]"
                            ),
                            Map.of(
                                    "path", "/admin/deleteGame/{gameID}",
                                    "method", "DELETE",
                                    "description", "Allows an admin to delete a specific game from the system",
                                    "Tags", "[Dynamic], [Log In Required], [Admin Only]"
                            ),
                            Map.of(
                                    "path", "/admin/addGenre",
                                    "method", "POST",
                                    "description", "Allows an admin to add a new genre to the system",
                                    "Tags", "[Log In Required], [Admin Only], [Requires Valid Request Body]"
                            ),
                            Map.of(
                                    "path", "/admin/deleteGenre/{genreID}",
                                    "method", "DELETE",
                                    "description", "Allows an admin to delete a specific genre from the system",
                                    "Tags", "[Dynamic], [Log In Required], [Admin Only]"
                            ),
                            Map.of(
                                    "path", "/admin/deleteUser/{userID}",
                                    "method", "DELETE",
                                    "description", "Allows an admin to delete a specific user account from the system",
                                    "Tags", "[Dynamic], [Log In Required], [Admin Only]"
                            )
                    ),
                    "authentication endpoints", List.of(
                            Map.of(
                                    "path", "/login",
                                    "method", "POST",
                                    "description", "Allows the user to log in to an existing account",
                                    "Tags", "[Requires Valid Request Body]"
                            ),
                            Map.of(
                                    "path", "/register",
                                    "method", "POST",
                                    "description", "Allows the user to register a new account",
                                    "Tags", "[Requires Valid Request Body]"
                            )
                    ),
                    "genre endpoints", List.of(
                            Map.of(
                                    "path", "/genres",
                                    "method", "GET",
                                    "description", "Allows the user to see all existing genres in the system",
                                    "Tags", "[Sortable], [Paginated]"
                            )
                    )
                )
        );

        return ResponseEntity.ok(response);
    }
}
