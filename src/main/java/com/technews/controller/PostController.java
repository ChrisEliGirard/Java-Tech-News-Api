package com.technews.controller;

import com.technews.model.Post;
import com.technews.model.User;
import com.technews.model.Vote;
import com.technews.repository.PostRepository;
import com.technews.repository.UserRepository;
import com.technews.repository.VoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

// UserController has most of the necessary comments for what this controller does as they are functionally the same except for the addVote() method
@RestController
public class PostController {

    @Autowired
    PostRepository repository;

    @Autowired
    VoteRepository voteRepository;

    @Autowired
    UserRepository userRepository;

    @GetMapping("/api/posts")
    public List<Post> getAllPosts() {
        List<Post> postList = repository.findAll();
        for (Post p : postList) {
            p.setVoteCount(voteRepository.countVotesByPostId(p.getId()));
        }
        return postList;
    }


    @GetMapping("/api/posts/{id}")
    public Post getPost(@PathVariable Integer id) {
        Post returnPost = repository.getById(id);
        returnPost.setVoteCount(voteRepository.countVotesByPostId(returnPost.getId()));

        return returnPost;
    }


    @PostMapping("/api/posts")
    @ResponseStatus(HttpStatus.CREATED)
    public Post addPost(@RequestBody Post post) {
        repository.save(post);
        return post;
    }


    @PutMapping("/api/posts/{id}")
    public Post updatePost(@PathVariable int id, @RequestBody Post post) {
        Post tempPost = repository.getById(id);
        tempPost.setTitle(post.getTitle());
        return repository.save(tempPost);
    }

    // addVote method here
    // @PutMapping("/api/posts/upvote") putRoute utilizes the addVote method to add a vote to a post
    @PutMapping("/api/posts/upvote")
    // Uses @RequestBody to assign incoming data to variables
    public String addVote(@RequestBody Vote vote, HttpServletRequest request) {
        // Initiates a String variable to be used in a return
        String returnValue = "";

        // if statement to check if the user is logged in before continuing
        if(request.getSession(false) != null) {
            // initiates a null returnPost var
            Post returnPost = null;

            // Gets the users data from their request using the getSession() method that is passed the getAttribute() method that will be grabbing the users session data
            User sessionUser = (User) request.getSession().getAttribute("SESSION_USER");
            // Sets the votes userId to the users id who made the vote from the sessionUser object by calling the getId() method on it
            vote.setUserId(sessionUser.getId());
            // saves the vote into the voteRepository
            voteRepository.save(vote);

            // Sets the returnPost var with the post data found via the vote data from the getPostId() method
            returnPost = repository.getById(vote.getPostId());
            // Sets the return post vote count by utilizing the countVotesByPostId() method
            returnPost.setVoteCount(voteRepository.countVotesByPostId(vote.getPostId()));

            // Sets the returnValue string to an empty string
            returnValue = "";

        } else {
            // If there was no session data set returnValue to "login"
            returnValue = "login";
        }

        // return the returnValue
        return returnValue;
    }


    @DeleteMapping("/api/posts/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletePost(@PathVariable int id) {
        repository.deleteById(id);
    }
}