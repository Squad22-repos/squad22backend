package com.example.squad22backend.dtos;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class RelationshipDTO {

    private List<String> followers;
    private List<String> following;
    private List<String> unfollowed;
    private List<String> blockers;
    private List<String> blocking;
    private List<String> unblocked;

    public void addToFollowers(String follower) {
        if (followers == null) {
            followers = new ArrayList<>();
        }
        followers.add(follower);
    }

    public void addToFollowing(String followingInput) {
        if (following == null) {
            following = new ArrayList<>();
        }
        following.add(followingInput);
    }

    public void addToUnfollowed(String unfollow) {
        if (unfollowed == null) {
            unfollowed = new ArrayList<>();
        }
        unfollowed.add(unfollow);
    }

    public void addToBlockers(String blocker) {
        if (blockers == null) {
            blockers = new ArrayList<>();
        }
        blockers.add(blocker);
    }

    public void addToBlocking(String blockingInput) {
        if (blocking == null) {
            blocking = new ArrayList<>();
        }
        blocking.add(blockingInput);
    }

    public void addToUnblocked(String unblock) {
        if (unblocked == null) {
            unblocked = new ArrayList<>();
        }
        unblocked.add(unblock);
    }
}
