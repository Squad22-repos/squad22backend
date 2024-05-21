package com.example.squad22backend.controllers;

import com.example.squad22backend.dtos.InteractionDTO;
import com.example.squad22backend.dtos.PostCreationDTO;
import com.example.squad22backend.infra.security.SecurityFilter;
import com.example.squad22backend.infra.security.TokenService;
import com.example.squad22backend.models.Post;
import com.example.squad22backend.models.User;
import com.example.squad22backend.models.UserPostRelation;
import com.example.squad22backend.repositories.PostRepository;
import com.example.squad22backend.repositories.UserRepository;
import com.example.squad22backend.services.PostService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("api/posts")
public class PostController {

    private final PostRepository postRepository;
    private final PostService postService;
    private final SecurityFilter securityFilter;
    private final TokenService tokenService;
    private final UserRepository userRepository;

    public PostController(PostRepository postRepository, PostService postService, SecurityFilter securityFilter, TokenService tokenService, UserRepository userRepository) {
        this.postRepository = postRepository;
        this.postService = postService;
        this.securityFilter = securityFilter;
        this.tokenService = tokenService;
        this.userRepository = userRepository;
    }

    public String identifyUser(HttpServletRequest request) {
        String token = this.securityFilter.recoverToken(request);
        return this.tokenService.getUsernameFromToken(token);
    }

    // Requisições GET
    @GetMapping
    public ResponseEntity<List<Post>> getAllPosts() {
        List<Post> posts = this.postRepository.findAll();
        return new ResponseEntity<>(posts, HttpStatus.OK);
    }

    @GetMapping("/{postId}")
    public ResponseEntity<Post> getAllPosts(@PathVariable String postId) {
        Post post = this.postRepository.findById(postId).orElseThrow(() -> new ResourceNotFoundException("Post not found"));
        return new ResponseEntity<>(post, HttpStatus.OK);
    }

    @GetMapping("/usuario/{username}")
    public ResponseEntity<List<Post>> getAllUserPosts(@PathVariable String username) {
        User user = (User) this.userRepository.findUserByUsername(username);
        List<Post> posts = this.postRepository.findByUser(user);
        return new ResponseEntity<>(posts, HttpStatus.OK);
    }

    @GetMapping("/interacao/salvos")
    public ResponseEntity<List<Post>> getAllUserInteractionsPosts(HttpServletRequest request, @RequestParam String interaction) {
        String username = this.identifyUser(request);
        User user = (User) this.userRepository.findUserByUsername(username);

        List<Post> savedPosts = this.postRepository.findUserSavedPosts(user.getId(), interaction);
        return new ResponseEntity<>(savedPosts, HttpStatus.OK);
    }

    @GetMapping("/interacao")
    public ResponseEntity<UserPostRelation> getSpecificUserPostRelation(HttpServletRequest request, @RequestParam String postId) {
        String username = this.identifyUser(request);
        User user = (User) this.userRepository.findUserByUsername(username);

        UserPostRelation usedPost = this.postRepository.findPostByUser(user.getId(), postId);
        return new ResponseEntity<>(usedPost, HttpStatus.OK);
    }

    // Requisições POST
    @PostMapping
    public ResponseEntity<String> createPost(HttpServletRequest request, @RequestBody PostCreationDTO postCreationDTO) {
        String authorUsername = this.identifyUser(request);
        LocalDateTime timeOfPost = LocalDateTime.now();

        String postId = this.postService.addPost(postCreationDTO, authorUsername, timeOfPost);
        return new ResponseEntity<>(postId, HttpStatus.CREATED);
    }

    @PostMapping("/interacao/{postId}")
    public ResponseEntity<?> interactWithPost(@PathVariable String postId, HttpServletRequest request, @RequestBody InteractionDTO interactionDTO) {
        String username = this.identifyUser(request);
        User user = (User) this.userRepository.findUserByUsername(username);

        String interactionId = postId + " by " + username;

        this.postRepository.addUserInteraction(interactionId, user.getId(), postId, interactionDTO.actionType(), interactionDTO.actionStatus(), interactionDTO.isLiked(), interactionDTO.isCommented());
        this.updateUserPostRelation(postId, request, interactionDTO);
        return ResponseEntity.ok().build();
    }

    // Requisições PUT
    @PutMapping("/{id}")
    public ResponseEntity<?> updatePost(@PathVariable String id, HttpServletRequest request, @RequestParam(required = false) String title, @RequestParam(required = false) String content, @RequestParam(required = false) String visibility) {
        String username = this.identifyUser(request);
        User user = (User) this.userRepository.findUserByUsername(username);
        Post post = this.postRepository.findById(id).orElseThrow(null);

        if (!post.getAuthor().getId().equals(user.getId()) && !user.getAccountType().toString().equals("ADMIN")) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        this.postService.updatePostData(id, title, content, visibility);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/interacao/{postId}")
    public ResponseEntity<?> updateUserPostRelation(@PathVariable String postId, HttpServletRequest request, @RequestBody InteractionDTO interactionDTO) {
        String username = this.identifyUser(request);
        User user = (User) this.userRepository.findUserByUsername(username);

        this.postService.updateUserPostRelation(postId, user, interactionDTO);
        return ResponseEntity.ok().build();
    }
}
