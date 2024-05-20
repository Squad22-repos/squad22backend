package com.example.squad22backend.services;

import com.example.squad22backend.dtos.RelationshipDTO;
import com.example.squad22backend.dtos.UserRegisterDTO;
import com.example.squad22backend.exceptions.CommunityNotFoundException;
import com.example.squad22backend.models.*;
import com.example.squad22backend.repositories.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final StudentRepository studentRepository;
    private final ProfessorRepository professorRepository;
    private final CommercialRepository commercialRepository;
    private final CommunityRepository communityRepository;

    public UserService(UserRepository userRepository, StudentRepository studentRepository, ProfessorRepository professorRepository, CommercialRepository commercialRepository, CommunityRepository communityRepository) {
        this.userRepository = userRepository;
        this.studentRepository = studentRepository;
        this.professorRepository = professorRepository;
        this.commercialRepository = commercialRepository;
        this.communityRepository = communityRepository;
    }

    public RelationshipDTO getSpecificUserRelationships(String id, String relationship) {
        RelationshipDTO relationshipDTO = new RelationshipDTO();
        User user = this.userRepository.findById(id).orElse(null);

        if (relationship != null && !relationship.isEmpty()) {
            String relationshipSuffix = relationship.substring(relationship.length() - 3);

            if (relationshipSuffix.equals("ing")) {
                List<String> relations = this.userRepository.findSpecificUserRelationsAsActor(user, relationship);

                switch (relationship) {
                    case "following" -> relationshipDTO.setFollowing(relations);
                    case "blocking" -> relationshipDTO.setBlocking(relations);
                    case "unfollowing" -> relationshipDTO.setUnfollowed(relations);
                    case "unblocking" -> relationshipDTO.setUnblocked(relations);
                }

            } else {
                List<String> relations = this.userRepository.findSpecificUserRelationsAsSubject(user, relationship);

                switch (relationship) {
                    case "followers" -> relationshipDTO.setFollowers(relations);
                    case "blockers" -> relationshipDTO.setBlockers(relations);
                }
            }

        } else {
            List<UserRelation> allRelations = this.userRepository.findAllUserRelations(user);

            for (UserRelation relation : allRelations) {
                String actorId = relation.getActor().getId();
                String subjectId = relation.getSubject().getId();
                String rel = relation.getRelationship();

                switch (rel) {
                    case "follows" -> {
                        if (actorId.equals(id)) {
                            relationshipDTO.addToFollowing(subjectId);
                        } else if (subjectId.equals(id)) {
                            relationshipDTO.addToFollowers(actorId);
                        }
                    }
                    case "blocks" -> {
                        if (actorId.equals(id)) {
                            relationshipDTO.addToBlocking(subjectId);
                        } else if (subjectId.equals(id)) {
                            relationshipDTO.addToBlockers(actorId);
                        }
                    }
                    case "unfollows" -> relationshipDTO.addToUnfollowed(subjectId);
                    case "unblocks" -> relationshipDTO.addToUnblocked(subjectId);
                }
            }
        }
        return relationshipDTO;
    }

    public void generateUserId(User user) {
        if (user.getRegistration() == 0 && user.getAccountType().toString().equals("COMERCIAL")) {
            int random = (int) ((Math.random() * (10000 - 1000)) + 1000);

            while (this.userRepository.registrationExists(random)) {
                random = (int) ((Math.random() * (10000 - 1000)) + 1000);
            }
            user.setRegistration(random);
        }

        String lastThree = String.valueOf(user.getRegistration() % 1000);
        String userSignature = user.getName().split(" ")[0]+ lastThree + user.getAccountType();

        int diferential = 0;
        while(this.userRepository.existsById(userSignature)) {
            userSignature = userSignature + diferential;
            diferential += 1;
        }

        user.setId(userSignature);
    }

    public void addUser(UserRegisterDTO user, String encryptedPassword) {
        User newUser = new User("", user.username(), user.name(), encryptedPassword, user.registration(), user.accountType(), user.activityStatus());
        this.generateUserId(newUser);

        this.userRepository.addUser(newUser.getId(), newUser.getUsername(), newUser.getName(), newUser.getPassword(),
                newUser.getRegistration(), newUser.getAccountType().toString(), newUser.getActivityStatus()
        );

        switch (newUser.getAccountType().toString().toUpperCase()) {
            case "ESTUDANTE" -> {
                Student student = new Student(newUser);
                student.setCourse(user.course());
                student.setClasses(user.classes());

                this.studentRepository.addStudent(
                        student.getId(), student.getCourse(), student.getClasses().toArray(new String[0])
                );
                this.studentRepository.addStudentClasses(student.getId(), student.getClasses().toArray(new String[0]));
            }
            case "PROFESSOR" -> {
                Professor professor = new Professor(newUser);
                professor.setClasses(user.classes());
                professor.setCourses(user.courses());
                professor.setDegrees(user.degrees());

                this.professorRepository.addProfessor(
                        professor.getId(), professor.getCourses().toArray(new String[0]), professor.getClasses().toArray(new String[0]), professor.getDegrees().toArray(new String[0])
                );
                this.professorRepository.addProfessorCourses(professor.getId(), professor.getCourses().toArray(new String[0]));
                this.professorRepository.addProfessorClasses(professor.getId(), professor.getClasses().toArray(new String[0]));
                this.professorRepository.addProfessorDegrees(professor.getId(), professor.getDegrees().toArray(new String[0]));
            }
            case "COMERCIAL" -> {
                CommercialUser commercialUser = new CommercialUser(newUser);
                commercialUser.setServices(user.services());
                commercialUser.setOpening(user.opening());
                commercialUser.setClosing(user.closing());
                commercialUser.setStores(user.stores());

                this.commercialRepository.addCommercial(
                        commercialUser.getId(), commercialUser.getStores().toArray(new String[0]), commercialUser.getServices().toArray(new String[0]),
                        commercialUser.getOpening(), commercialUser.getClosing()
                );
                this.commercialRepository.addCommercialStores(commercialUser.getId(), commercialUser.getStores().toArray(new String[0]));
                this.commercialRepository.addCommercialServices(commercialUser.getId(), commercialUser.getServices().toArray(new String[0]));
            }
        }
    }

    public void addRelationship(UserRelation relationship) {
        relationship.setId(relationship.getActor().getId() + "-to-" + relationship.getSubject().getId());

        this.userRepository.addUserRelationship(relationship.getId(), relationship.getActor().getId(), relationship.getSubject().getId(), relationship.getRelationship());
    }

    public void updateRelationshipBetweenUsers(String actorId, String newRelationship, String subjectId) {
        User actingUser = this.userRepository.findById(actorId).orElse(null);
        User subjectedUser = this.userRepository.findById(subjectId).orElse(null);

        this.userRepository.updateRelationshipBetweenUsers(actingUser, newRelationship, subjectedUser);
    }

    public void updateUser(String id, String newUsername, String newName, String newStatus) {
        if (newUsername != null && !newUsername.isEmpty()) {
            this.userRepository.updateUsername(id, newUsername);
        }
        if (newName != null && !newName.isEmpty()) {
            this.userRepository.updateName(id, newName);
        }
        if (newStatus != null && !newStatus.isEmpty()) {
            this.userRepository.updateActivity(id, newStatus);
        }
    }

    public Optional<UserRelation> findRelation(String actorId, String subjectId) {
        User actor = this.userRepository.findById(actorId).orElse(null);
        User subject = this.userRepository.findById(subjectId).orElse(null);

        return this.userRepository.findRelation(actor, subject);
    }

    public List<Community> getUserCommunities(String id) {
        List<String> communitiesId = this.communityRepository.getUserCommunities(id);
        List<Community> communities = new ArrayList<>();

        for (String communityId : communitiesId) {
            communities.add(this.communityRepository.findById(communityId).orElseThrow(() -> new CommunityNotFoundException(communityId)));
        }
        return communities;
    }

}
