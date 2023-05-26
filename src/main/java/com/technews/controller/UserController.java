package com.technews.controller;

import com.technews.model.Post;
import com.technews.model.User;
import com.technews.repository.UserRepository;
import com.technews.repository.VoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class UserController {
    // @Autowired tells Spring to scan the project for objects that will need to be instantiated for a class or method to run
    // Thereby allowing instantiating objects only when their needed
    @Autowired
    UserRepository repository;

    @Autowired
    VoteRepository voteRepository;

    // @GetMapping("/api/users") annotation on the getAllUsers() method combines the route ("/api/users") and the HTTP method (GET), providing the method with a unique endpoint.
    @GetMapping("/api/users")
    public List<User> getAllUsers() {
        // uses the findAll() method of repository and assigns the resulting list to the userList variable
        List<User> userList = repository.findAll();
        // For loop to do the following for every user in the list referenced as u
        for (User u : userList) {
            // Gets the current users posts via the getPosts method from the user models
            List<Post> postList = u.getPosts();
            // Then each post that the user has is iterated through, invoking the setVoteCount() method, passing in the countVotesByPostId() method, and finally using getId() to obtain the id of the post.
            for (Post p : postList) {
                p.setVoteCount(voteRepository.countVotesByPostId(p.getId()));
            }
        }
        // Returns the populated userList variable
        return userList;
    }

    // Similar to the getRoute above except for a single user, found by id
    @GetMapping("/api/users/{id}")
    public User getUserById(@PathVariable Integer id) {
        User returnUser = repository.getById(id);
        List<Post> postList = returnUser.getPosts();
        for (Post p : postList) {
            p.setVoteCount(voteRepository.countVotesByPostId(p.getId()));
        }

        return returnUser;
    }

    // @PostMapping("/api/users") annotation on the addUser() method combines the route ("/api/users") and the HTTP method (POST), inside the addUser() method we pass in the annotation @RequestBody
    // The @RequestBody annotation maps the body of this request to a transfer object, then deserializes the body onto a Java object for easier use
    @PostMapping("/api/users")
    public User addUser(@RequestBody User user) {
        // Encrypt password and set the encrypted password as the users password using BCrypt
        user.setPassword(BCrypt.hashpw(user.getPassword(), BCrypt.gensalt()));
        // Saves the new user with their encrypted password
        repository.save(user);
        // return the newly created user
        return user;
    }

    // @PutMapping("/api/users/{id}") annotation allows us to update a user from their specific id using the updateUser() method
    @PutMapping("/api/users/{id}")
    // The @PathVariable enables us to enter the int id into the request URI as a parameter, replacing the {id} in the route
    public User updateUser(@PathVariable int id, @RequestBody User user) {
        // Gets userdata by id using getById and sets it to a tempUser variable
        User tempUser = repository.getById(id);

        // If the tempUser variable isn't null after the search (i.e theres actually a user with that id) then run the following statement
        if (!tempUser.equals(null)) {
            // sets the incoming user update id with the tempUser id
            user.setId(tempUser.getId());
            // saves the new userdata for that userId
            repository.save(user);
        }
        // return the updated user data
        return user;
    }

    // @DeleteMapping("/api/users/{id}") annotation allows us to delete a user based on their specific id using the deleteUser() method
    @DeleteMapping("/api/users/{id}")
    // @ResponseStatus allows us to return a status instead of data as there will be nothing to return after the user is deleted
    @ResponseStatus(HttpStatus.NO_CONTENT)
    // Uses @PathVariable as before to replace {id} with an int id in the Request URI
    public void deleteUser(@PathVariable int id) {
        // Deletes the user from the repository by id
        repository.deleteById(id);
    }
}
