package com.example.squad22backend.services;

import com.example.squad22backend.dtos.InteractionDTO;
import com.example.squad22backend.dtos.PostCreationDTO;
import com.example.squad22backend.exceptions.UserNotFoundException;
import com.example.squad22backend.models.Post;
import com.example.squad22backend.models.User;
import com.example.squad22backend.models.UserPostRelation;
import com.example.squad22backend.repositories.CommunityRepository;
import com.example.squad22backend.repositories.PostRepository;
import com.example.squad22backend.repositories.UserRepository;
import org.antlr.v4.runtime.misc.LogManager;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Service
public class PostService {

    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final CommunityRepository communityRepository;

    public PostService(UserRepository userRepository, PostRepository postRepository, CommunityRepository communityRepository) {
        this.userRepository = userRepository;
        this.postRepository = postRepository;
        this.communityRepository = communityRepository;
    }

    public List<String> getKeywords(String postTitle, String postContent) {
        List<String> titleWords = Arrays.asList(postTitle.split("\\s+"));
        List<String> contentWords = Arrays.asList(postContent.split("\\s+"));

        List<String> combinedWords = new ArrayList<>();
        combinedWords.addAll(titleWords);
        combinedWords.addAll(contentWords);

        Collections.shuffle(combinedWords);

        return combinedWords.subList(0, Math.min(5, combinedWords.size()));
    }

    public String addPost(PostCreationDTO post, String authorUsername, LocalDateTime timeOfPost) {
        User author = (User) this.userRepository.findUserByUsername(authorUsername);

        List<String> keywords = this.getKeywords(post.title(), post.content());
        
        String postId = post.title() + "-by-" + author.getUsername() + "-at-" + timeOfPost.toString();
        Post newPost = new Post(postId, post.title(), post.content(), author, timeOfPost, post.likes(), post.visibility(), keywords);
        
        this.postRepository.addPost(newPost.getId(), newPost.getTitle(), newPost.getContent(), newPost.getAuthor().getId(),
        newPost.getPostDate(), newPost.getLikes(), newPost.getVisibility(), newPost.getKeywords().toArray(new String[0]));

        if (!(post.communityId().isEmpty() || post.communityPostStatus().isEmpty())) {
            String postInCommunityId = post.title() + "-in-" + post.communityId();
            this.communityRepository.addPostToCommunity(postInCommunityId, post.communityId(), postId, post.communityPostStatus());
        }

        return newPost.getId();
    }

    public void updatePostData(String id, String title, String content, String visibility) {
        if (title != null) {
            this.postRepository.updateTitle(id, title);
        }
        if (content != null) {
            this.postRepository.updateContent(id, content);
        }
        if (visibility != null) {
            this.postRepository.updateVisibility(id, visibility);
        }
    }

    public void updateUserPostRelation(String postId, User user, InteractionDTO interactionDTO) {
        UserPostRelation post = this.postRepository.findPostByUser(user.getId(), postId);

        if (interactionDTO.actionType() != null) {
            this.postRepository.updatePostActionType(post.getId(), interactionDTO.actionType());
        }
        if (interactionDTO.actionStatus() != null) {
            this.postRepository.updatePostActionStatus(post.getId(), interactionDTO.actionStatus());
        }
        if (interactionDTO.isLiked() != null) {
            this.postRepository.updatePostLike(post.getId(), interactionDTO.isLiked());
            this.likePost(postId, user.getId());
        }
        if (interactionDTO.isCommented() != null) {
            this.postRepository.updatePostComment(post.getId(), interactionDTO.isCommented());
        }
    }

    public void likePost(String postId, String userId) {
        Post post = this.postRepository.findById(postId).orElseThrow(null);
        List<UserPostRelation> likedPosts = this.postRepository.findPostLikes(postId);
        post.setLikes(likedPosts.size());
        if (this.postRepository.findPostByUser(userId, postId) == null) {
            this.postRepository.save(post);
        } else {
            this.postRepository.updatePostLike(post.getId(), false);
        }
    }
}
