package com.example.squad22backend.repositories;

import com.example.squad22backend.models.Post;
import com.example.squad22backend.models.User;
import com.example.squad22backend.models.UserPostRelation;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface PostRepository extends JpaRepository<Post, String> {

    @Query("from Post where author = :user")
    List<Post> findByUser(@Param("user") User user);

    @Query(value = "select upi.post from UserPostRelation upi where upi.actor.id = :userId and upi.actionType = :action")
    List<Post> findUserSavedPosts(@Param("userId") String userId, @Param("action") String action);

    @Query(value = "from UserPostRelation upi where upi.actor.id = :userId and upi.post.id = :postId")
    UserPostRelation findPostByUser(@Param("userId") String userId, @Param("postId") String postId);

    @Query(value = "from UserPostRelation upi where upi.post.id = :postId and upi.isLiked = true")
    List<UserPostRelation> findPostLikes(@Param("postId") String postId);

    @Modifying
    @Query(value = "INSERT INTO post (id, title, content, author_id, post_date, likes, visibility, keywords) " +
            "VALUES (:id, :title, :content, :authorId, :postDate, :likes, :visibility, :keywords)", nativeQuery = true)
    @Transactional
    void addPost(@Param("id") String id, @Param("title") String title, @Param("content") String content,
                 @Param("authorId") String authorId, @Param("postDate") LocalDateTime postDate,
                 @Param("likes") Integer likes, @Param("visibility") String visibility,
                 @Param("keywords") String[] keywords);

    @Modifying
    @Query(value = "INSERT INTO user_post_interaction (interaction_id, user_id, post_id, action_type, action_status, is_liked, is_commented) " +
            "VALUES (:interactionId, :userId, :postId, :actionType, :actionStatus, :isLiked, :isCommented)", nativeQuery = true)
    @Transactional
    void addUserInteraction(@Param("interactionId") String interactionId, @Param("userId") String userId, @Param("postId") String postId,
                            @Param("actionType") String actionType, @Param("actionStatus") String actionStatus, @Param("isLiked") Boolean isLiked,
                            @Param("isCommented") Boolean isCommented);

    @Modifying
    @Query(value = "update post set title = :title where id = :id", nativeQuery = true)
    @Transactional
    void updateTitle(@Param("id") String id, @Param("title") String title);

    @Modifying
    @Query(value = "update post set content = :content where id = :id", nativeQuery = true)
    @Transactional
    void updateContent(@Param("id") String id, @Param("content") String content);

    @Modifying
    @Query(value = "update post set title = :visibility where id = :id", nativeQuery = true)
    @Transactional
    void updateVisibility(@Param("id") String id, @Param("visibility") String visibility);

    @Modifying
    @Query(value = "update user_post_interaction set action_type = :actionType where interaction_id = :id", nativeQuery = true)
    @Transactional
    void updatePostActionType(@Param("id") String id,@Param("actionType") String actionType);

    @Modifying
    @Query(value = "update user_post_interaction set action_status = :actionStatus where interaction_id = :postId", nativeQuery = true)
    @Transactional
    void updatePostActionStatus(@Param("postId") String postId, @Param("actionStatus") String actionStatus);

    @Modifying
    @Query(value = "update user_post_interaction set is_liked = :isLiked where interaction_id = :postId", nativeQuery = true)
    @Transactional
    void updatePostLike(@Param("postId") String postId, @Param("isLiked") boolean isLiked);

    @Modifying
    @Query(value = "update user_post_interaction set is_commented = :isCommented where interaction_id = :postId", nativeQuery = true)
    @Transactional
    void updatePostComment(@Param("postId") String postId, @Param("isCommented") boolean isCommented);
}
