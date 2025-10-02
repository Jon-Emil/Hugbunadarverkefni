package is.hi.hbv501g.hbv1.controllers;

import is.hi.hbv501g.hbv1.extras.PaginatedResponse;
import is.hi.hbv501g.hbv1.persistence.entities.Game;
import is.hi.hbv501g.hbv1.persistence.entities.User;
import is.hi.hbv501g.hbv1.services.GameService;
import is.hi.hbv501g.hbv1.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;

@RestController
public class UserController {
    private final UserService userService;
    private final GameService gameService;

    @Autowired
    public UserController(UserService userService, GameService gameService) {
        this.userService = userService;
        this.gameService = gameService;
    }

    @RequestMapping(value = "/users", method = RequestMethod.GET)
    public PaginatedResponse<User> allUsers(
            @RequestParam(defaultValue = "1") int pageNr,
            @RequestParam(defaultValue = "10") int perPage
    ) {
        //Business logic
        //Call a method in a service class
        //Add some data to the model
        // we only return data not HTML templates
        List<User> allUsers = userService.findAll();
        return new PaginatedResponse<User>(200, allUsers, pageNr,perPage);
    }


    @RequestMapping(value = "/users", method = RequestMethod.POST)
    public User addUser(
            @RequestBody User user
    ) {
        return userService.save(user);
    }

        // 添加收藏
    @PostMapping("/users/{userId}/favorites/{gameId}")
    public Map<String,Object> addFavouriteGames(@PathVariable Long gameId, @PathVariable Long userId) {
        // 用 GameService 校验游戏存在即可（不直接碰 repository）
        if (gameService.findById(gameId) == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Game not found");
        }
        boolean added = userService.addFavouriteGames(gameId, userId);
        return Map.of("added", added);
    }

    // 查询收藏列表
    @GetMapping("/users/{userId}/favorites")
    public List<Game> getFavouriteGames(@PathVariable Long userId) {
        return userService.getFavouriteGames(userId);
    }

    // 取消收藏
    @DeleteMapping("/users/{userId}/favorites/{gameId}")
    public Map<String,Object> deleteFavouriteGames(@PathVariable Long gameId, @PathVariable Long userId) {
        if (gameService.findById(gameId) == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Game not found");
        }
        boolean removed = userService.deleteFavouriteGames(gameId, userId);
        return Map.of("removed", removed);
    }
}
