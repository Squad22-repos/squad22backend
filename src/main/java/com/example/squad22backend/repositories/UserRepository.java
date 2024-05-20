package com.example.squad22backend.repositories;

import com.example.squad22backend.models.User;
import com.example.squad22backend.models.UserRelation;
import com.example.squad22backend.models.UserRole;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, String> {

      // Verificar se a matricula já existe matricula
      @Query("select case when count(u)> 0 then true else false end from user_table u where u.registration = :registration_info")
      boolean registrationExists(@Param("registration_info") int registration_info);

      // Buscar usuários por uma conta
      @Query("from user_table where accountType = :accountType")
      List<User> findUsersByAccount(@Param("accountType") UserRole accountType);

      // Buscar usuário por username
      @Query("from user_table where username = :username")
      UserDetails findUserByUsername(@Param("username") String username);

      // Buscar relações entre usuários
      @Query("From UserRelation where actor = :actorId and subject = :subjectId")
      Optional<UserRelation> findRelation(@Param("actorId") User actorId, @Param("subjectId") User subjectId);

      @Query("from UserRelation where actor = :userId or subject = :userId")
      List<UserRelation> findAllUserRelations(@Param("userId") User userId);

      @Query("select actor from UserRelation where actor = :userId and relationship = :rel")
      List<String> findSpecificUserRelationsAsActor(@Param("userId") User userId, @Param("rel") String rel);

      @Query("select subject from UserRelation where subject = :userId and relationship = :rel")
      List<String> findSpecificUserRelationsAsSubject(@Param("userId") User userId, @Param("rel") String rel);

      // Adicionar usuários e suas contas especificas
      @Modifying
      @Query(value = "insert into user_table (id, username, full_name, password, registration_info, account_type, activity_status)" +
              "VALUES (:id, :username, :full_name, :password, :registration, :account, :activity)", nativeQuery = true)
      @Transactional
      void addUser(@Param("id") String id, @Param("username") String username, @Param("full_name") String full_name,
                   @Param("password") String password, @Param("registration") int registration,
                   @Param("account") String account, @Param("activity") String activity);

      // Adcionar relação entre usuários
      @Modifying
      @Query(value = "insert into user_relation (relation_id, actor_id, subject_id, relation_type)" +
              "VALUES (:relationId, :actorId, :subjectId, :relationType)", nativeQuery = true)
      @Transactional
      void addUserRelationship(@Param("relationId") String relationId, @Param("actorId") String actorId, @Param("subjectId") String subjectId, @Param("relationType") String relationType);

      // Atualizar relação entre usuários
      @Modifying
      @Query("update UserRelation set relationship = :newRelation where actor = :actorId and subject = :subjectId")
      @Transactional
      void updateRelationshipBetweenUsers(@Param("actorId") User actorId, @Param("newRelation") String newRelation, @Param("subjectId") User subjectId);

      //Atualizar dados de usuário
      @Modifying
      @Query("update user_table u set u.username = :newUsername where u.id = :id")
      @Transactional
      void updateUsername(@Param("id") String id, @Param("newUsername") String newUsername);

      @Modifying
      @Query("update user_table u set u.name = :newName where u.id = :id")
      @Transactional
      void updateName(@Param("id") String id, @Param("newName") String newName);

      @Modifying
      @Query("update user_table u set u.activityStatus = :newActivity where u.id = :id")
      @Transactional
      void updateActivity(@Param("id") String id, @Param("newActivity") String newActivity);

      @Modifying
      @Query(value = "delete from user_relation where actor_id = :actorId and subject_id = :subjectId", nativeQuery = true)
      @Transactional
      void removeUserRelation(@Param("actorId") String actorId, @Param("subjectId") String subjectId);
}