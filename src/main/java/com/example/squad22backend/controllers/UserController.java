package com.example.squad22backend.controllers;

import com.example.squad22backend.dtos.RelationshipDTO;
import com.example.squad22backend.exceptions.UserNotFoundException;
import com.example.squad22backend.models.*;
import com.example.squad22backend.repositories.UserRepository;
import com.example.squad22backend.services.CommercialServices;
import com.example.squad22backend.services.ProfessorService;
import com.example.squad22backend.services.StudentService;
import com.example.squad22backend.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalTime;
import java.util.List;

@RestController
@RequestMapping("api/usuarios")
public class UserController {

    private final UserService userService;
    private final UserRepository userRepository;
    private final StudentService studentService;
    private final ProfessorService professorService;
    private final CommercialServices commercialService;

    @Autowired
    public UserController(UserService userService, UserRepository userRepository, StudentService studentService, ProfessorService professorService, CommercialServices commercialService) {
        this.userService = userService;
        this.userRepository = userRepository;
        this.studentService = studentService;
        this.professorService = professorService;
        this.commercialService = commercialService;
    }

    // GET Methods
    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = this.userRepository.findAll();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @GetMapping("/conta/{tipo}")
    @ResponseStatus(HttpStatus.OK)
    public List<User> getAllStudentsByType(@PathVariable String tipo) {
        UserRole accountType = UserRole.valueOf(tipo.toUpperCase());
        return this.userRepository.findUsersByAccount(accountType);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getSpecificUser(@PathVariable String id) {
        User user = this.userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @GetMapping("/username/{username}")
    public ResponseEntity<?> getSpecificUserByName(@PathVariable String username) {
        User user = (User) this.userRepository.findUserByUsername(username);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @GetMapping("/{id}/comunidades")
    public ResponseEntity<List<Community>> getSpecificUserComunidades(@PathVariable String id) {
        List<Community> userCommunities = this.userService.getUserCommunities(id);
        return new ResponseEntity<>(userCommunities, HttpStatus.OK);
    }

    @GetMapping("/{id}/relacionamentos")
    public ResponseEntity<RelationshipDTO> getSpecificUserRelationships(@PathVariable String id, @RequestParam(required = false) String relationship) {
        RelationshipDTO userRelationships = userService.getSpecificUserRelationships(id, relationship);
        return ResponseEntity.ok(userRelationships);
    }

    // POST Methods
    @PostMapping("/{id}/relacionamentos")
    public ResponseEntity<?> postRelationship(@PathVariable String id, @RequestBody UserRelation relationship) {
        this.userService.addRelationship(relationship);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    //PUT Methods
    @PutMapping("/{id}")
    public ResponseEntity<?> putUserData(@PathVariable String id, @RequestParam(required = false) String newUsername, @RequestParam(required = false) String newName, @RequestParam(required = false) String newStatus) {
        this.userService.updateUser(id, newUsername, newName, newStatus);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/estudante/{id}")
    public ResponseEntity<?> putStudentData(@PathVariable String id, @RequestParam(required = false) String newCourse, @RequestParam(required = false) String[] newClasses) {
        this.studentService.updateStudentData(id, newCourse, newClasses);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/professor/{id}")
    public ResponseEntity<?> putProfessorData(@PathVariable String id, @RequestParam(required = false) String[] newCourses, @RequestParam(required = false) String[] newClasses, @RequestParam(required = false) String[] newDegrees) {
        this.professorService.updateProfessorData(id, newCourses, newClasses, newDegrees);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/comercial/{id}")
    public ResponseEntity<?> putComercialData(@PathVariable String id, @RequestParam(required = false) String[] stores, @RequestParam(required = false) String[] services, @RequestParam(required = false) LocalTime opening, @RequestParam(required = false) LocalTime closing) {
        this.commercialService.updateCommercialUserData(id, stores, services, opening, closing);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/{actorId}/relacionamentos")
    public ResponseEntity<?> putUserRelationship(@PathVariable String actorId, @RequestParam String subjectId, @RequestParam String newRelationship) {
        this.userService.updateRelationshipBetweenUsers(actorId, newRelationship, subjectId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    //DELETE Methods
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUserData(@PathVariable String id) {
        if (this.userRepository.existsById(id)) {
            this.userRepository.deleteById(id);
            return ResponseEntity.ok("Usuário de ID " + id + " não existe mais.");
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{actorId}/relacionamentos")
    public ResponseEntity<String> deleteUserRelation(@PathVariable String actorId, @RequestParam String subjectId) {
        UserRelation relation = this.userService.findRelation(actorId, subjectId).orElse(null);
        if (relation != null) {
            this.userRepository.removeUserRelation(actorId, subjectId);
            return ResponseEntity.ok("Relações entre usuários " + actorId + " e " + subjectId + "deletada com sucesso.");
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
