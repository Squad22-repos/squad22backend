package com.example.squad22backend.repositories;

import com.example.squad22backend.models.Professor;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ProfessorRepository extends JpaRepository<Professor, String> {

    // Adicionar professor na tabela professor
    @Modifying
    @Query(value = "insert into professor (user_id, courses, classes, degrees)" +
            "VALUES (:id, :courses, :classes, :degrees)", nativeQuery = true)
    @Transactional
    void addProfessor(@Param("id") String id, @Param("courses") String[] courses, @Param("classes") String[] classes, @Param("degrees") String[] degrees);

    // Adicionar dados do professor em suas respectivas tabelas
    @Modifying
    @Query(value = "insert into professor_courses (professor_user_id, courses)" +
            "VALUES (:id, :courses)", nativeQuery = true)
    @Transactional
    void addProfessorCourses(@Param("id") String id, @Param("courses") String[] courses);

    @Modifying
    @Query(value = "insert into professor_classes (professor_user_id, classes)" +
            "VALUES (:id, :classes)", nativeQuery = true)
    @Transactional
    void addProfessorClasses(@Param("id") String id, @Param("classes") String[] classes);

    @Modifying
    @Query(value = "insert into professor_degrees (professor_user_id, degrees)" +
            "VALUES (:id, :degrees)", nativeQuery = true)
    @Transactional
    void addProfessorDegrees(@Param("id") String id, @Param("degrees") String[] degrees);

    // Atualizar dados especificos do professor
    @Modifying
    @Query(value = "update Professor set courses = :newCourses where user_id = :id", nativeQuery = true)
    @Transactional
    void updateProfessorCourses(@Param("id") String id, @Param("newCourses") String[] newCourses);

    @Modifying
    @Query(value = "update Professor set classes = :newClasses where user_id = :id", nativeQuery = true)
    @Transactional
    void updateProfessorClasses(@Param("id") String id, @Param("newClasses") String[] newClasses);

    @Modifying
    @Query(value = "update Professor set degrees = :newDegrees where user_id = :id", nativeQuery = true)
    @Transactional
    void updateProfessorDegrees(@Param("id") String id, @Param("newDegrees") String[] newDegrees);

    //Atualizar dados especificos do professor em suas respectivas tabelas
    @Modifying
    @Query(value = "update professor_courses set courses = :newCourses where professor_user_id = :id", nativeQuery = true)
    @Transactional
    void updateProfessorCoursesTable(@Param("id") String id, @Param("newCourses") String[] newCourses);

    @Modifying
    @Query(value = "update professor_classes set classes = :newClasses where professor_user_id = :id", nativeQuery = true)
    @Transactional
    void updateProfessorClassesTable(@Param("id") String id, @Param("newClasses") String[] newClasses);

    @Modifying
    @Query(value = "update professor_degrees set degrees = :newDegrees where professor_user_id = :id", nativeQuery = true)
    @Transactional
    void updateProfessorDegreesTable(@Param("id") String id, @Param("newDegrees") String[] newDegrees);
}
