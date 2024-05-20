package com.example.squad22backend.services;

import com.example.squad22backend.exceptions.UserNotFoundException;
import com.example.squad22backend.models.User;
import com.example.squad22backend.repositories.CommunityRepository;
import com.example.squad22backend.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CommunityService {

    private final CommunityRepository communityRepository;
    private final UserRepository userRepository;

    public CommunityService(CommunityRepository communityRepository, UserRepository userRepository) {
        this.communityRepository = communityRepository;
        this.userRepository = userRepository;
    }

    public List<User> getCommunityMembers(String communityId) {
        List<String> membersId = this.communityRepository.getCommunityMembers(communityId);
        List<User> members = new ArrayList<>();

        for (String memberId : membersId) {
            members.add(this.userRepository.findById(memberId).orElseThrow(() -> new UserNotFoundException(memberId)));
        }
        return members;
    }

    public void updateCommunity(String id, String theme, String description, String visibility) {
        if (theme != null && !theme.isEmpty()) {
            this.communityRepository.updateTheme(id, theme);
        }
        if (description != null && !description.isEmpty()) {
            this.communityRepository.updateDescription(id, description);
        }
        if (visibility != null && !visibility.isEmpty()) {
            this.communityRepository.updateVisibility(id, visibility);
        }
    }
}
