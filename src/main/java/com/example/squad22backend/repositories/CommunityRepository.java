package com.example.squad22backend.repositories;

import com.example.squad22backend.models.Community;
import com.example.squad22backend.models.Post;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CommunityRepository extends JpaRepository<Community, String> {

    Optional<Community> findByTitle(String title);

    @Query(value = "select user_id from community_user_relation where community_id = :communityId", nativeQuery = true)
    List<String> getCommunityMembers(@Param("communityId") String communityId);

    @Query(value = "select community_id from community_user_relation where user_id = :userId", nativeQuery = true)
    List<String> getUserCommunities(@Param("userId") String userId);

    @Query(value = "select post_id from community_post where community_id = :communityId", nativeQuery = true)
    List<Post> getCommunityPosts(@Param("communityId") String communityId);

    @Modifying
    @Query(value = "INSERT INTO community_user_relation (relation_id, user_id, community_id, membership_status)" +
            "VALUES (:relationId, :userId, :communityId, :membership)", nativeQuery = true)
    @Transactional
    void addUserMembership(@Param("relationId") String relationId,
                           @Param("userId") String userId,
                           @Param("communityId") String communityId,
                           @Param("membership") String membership);

    @Modifying
    @Query(value = "INSERT INTO community_post (relation_id, community_id, post_id, post_status)" +
            "VALUES (:relationId, :communityId, :postId, :postStatus)", nativeQuery = true)
    @Transactional
    void addPostToCommunity(@Param("relationId") String relationId, @Param("communityId") String communityId, @Param("postId") String postId, @Param("postStatus") String postStatus);

    // As atualizações
    @Modifying
    @Query(value = "update community set theme = :theme where id = :id", nativeQuery = true)
    @Transactional
    void updateTheme(@Param("id") String id,@Param("theme") String theme);

    @Modifying
    @Query(value = "update community set description = :description where id = :id", nativeQuery = true)
    @Transactional
    void updateDescription(@Param("id") String id,@Param("description") String description);

    @Modifying
    @Query(value = "update community set visibility = :visibility where id = :id", nativeQuery = true)
    @Transactional
    void updateVisibility(@Param("id") String id,@Param("visibility") String visibility);

    @Modifying
    @Query(value = "update community_user_relation set membership_status = :newMembership where community_id = :communityId and user_id = :userId", nativeQuery = true)
    @Transactional
    void updateMembership(@Param("communityId") String communityId, @Param("userId") String userId, @Param("newMembership") String newMembership);
}
