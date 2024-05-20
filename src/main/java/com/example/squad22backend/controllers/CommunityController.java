package com.example.squad22backend.controllers;

import com.example.squad22backend.dtos.CommunityCreationDTO;
import com.example.squad22backend.exceptions.CommunityNotFoundException;
import com.example.squad22backend.infra.security.SecurityFilter;
import com.example.squad22backend.infra.security.TokenService;
import com.example.squad22backend.models.Community;
import com.example.squad22backend.models.CommunityUserRelation;
import com.example.squad22backend.models.Post;
import com.example.squad22backend.models.User;
import com.example.squad22backend.repositories.CommunityRepository;
import com.example.squad22backend.repositories.UserRepository;
import com.example.squad22backend.services.CommunityService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/comunidades")
public class CommunityController {

    private final CommunityRepository communityRepository;
    private final CommunityService communityService;
    private final UserRepository userRepository;
    private final SecurityFilter securityFilter;
    private final TokenService tokenService;

    public CommunityController(CommunityRepository communityRepository, CommunityService communityService, UserRepository userRepository, SecurityFilter securityFilter, TokenService tokenService) {
        this.communityRepository = communityRepository;
        this.communityService = communityService;
        this.userRepository = userRepository;
        this.securityFilter = securityFilter;
        this.tokenService = tokenService;
    }

    public String identifyUser(HttpServletRequest request) {
        String token = this.securityFilter.recoverToken(request);
        return this.tokenService.getUsernameFromToken(token);
    }

    // Requisições GET
    @GetMapping
    public ResponseEntity<List<Community>> getAllCommunities() {
        List<Community> communities = this.communityRepository.findAll();
        return new ResponseEntity<>(communities, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Community> getSpecificCommunity(@PathVariable String id) {
        Community community = this.communityRepository.findById(id).orElseThrow(() -> new CommunityNotFoundException(id));
        return new ResponseEntity<>(community, HttpStatus.OK);
    }

    @GetMapping("/{title}")
    public ResponseEntity<Community> getSpecificCommunityByTitle(@PathVariable String title) {
        Community community = this.communityRepository.findByTitle(title).orElseThrow(null);
        return new ResponseEntity<>(community, HttpStatus.OK);
    }

    @GetMapping("/{communityId}/membros")
    public ResponseEntity<List<User>> getMembersOfCommunity(@PathVariable String communityId) {
        List<User> members = this.communityService.getCommunityMembers(communityId);
        return new ResponseEntity<>(members, HttpStatus.OK);
    }

    @GetMapping("/{communityId}/posts")
    public ResponseEntity<List<Post>> getPostsOfCommunity(@PathVariable String communityId) {
        List<Post> posts = this.communityRepository.getCommunityPosts(communityId);
        return new ResponseEntity<>(posts, HttpStatus.OK);
    }

    // Requisições POST
    @PostMapping()
    public ResponseEntity<String> postCommunity(HttpServletRequest request, @RequestBody CommunityCreationDTO newCommunity) {
        String username = this.identifyUser(request);
        User user = (User) this.userRepository.findUserByUsername(username);

        Community community = new Community();
        community.setCreator(user);
        community.setTitle(newCommunity.title());
        community.setDescription(newCommunity.description());
        community.setTheme(newCommunity.theme());
        community.setVisibility(newCommunity.visibility());

        String relationId = community.getId() + "-by-" + user.getId();
        this.communityRepository.save(community);
        this.communityRepository.addUserMembership(relationId, user.getId(), community.getId(), "creator");
        Community communityId = this.communityRepository.findByTitle(newCommunity.title()).orElseThrow(() -> new CommunityNotFoundException(newCommunity.title()));
        return new ResponseEntity<>(communityId.getId(), HttpStatus.OK);
    }

    @PostMapping("/membros")
    public ResponseEntity<?> postCommunityMember(@RequestBody CommunityUserRelation membership) {
        this.communityRepository.addUserMembership(membership.getRelationId(), membership.getUserId().getId(), membership.getCommunityId().getId(), membership.getMembershipStatus());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    // Requisições PUT
    @PutMapping("/{id}")
    public ResponseEntity<?> updateCommunity(@PathVariable String id, @RequestParam(required = false) String theme, @RequestParam(required = false) String description, @RequestParam(required = false) String visibility) {
        this.communityService.updateCommunity(id, theme, description, visibility);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/membros/{communityId}")
    public ResponseEntity<?> updateCommunityMembers(@PathVariable String communityId, @RequestParam() String newMembership, @RequestParam() String userId) {
        this.communityRepository.updateMembership(communityId, userId, newMembership);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    // Requisições DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCommunity(@PathVariable String id) {
        this.communityRepository.deleteById(id);
        return new ResponseEntity<>("Comunidade de ID " + id + " não existe mais.", HttpStatus.OK);
    }
}
