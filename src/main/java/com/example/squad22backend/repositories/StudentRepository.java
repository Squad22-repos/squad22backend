package com.example.squad22backend.repositories;

import com.example.squad22backend.models.Student;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface StudentRepository extends JpaRepository<Student, String> {

    // Adiciona estudante
    @Modifying
    @Query(value = "insert into student (user_id, course, classes) VALUES (:id, :course, :classes)", nativeQuery = true)
    @Transactional
    void addStudent(@Param("id") String id, @Param("course") String course, @Param("classes") String[] classes);

    // Adiciona as aulas do estudante a tabela respectiva
    @Modifying
    @Query(value = "insert into student_classes (student_user_id, classes) VALUES (:id, :classes)", nativeQuery = true)
    @Transactional
    void addStudentClasses(@Param("id") String id, @Param("classes") String[] classes);

    // Atualiza as aulas do estudante
    @Modifying
    @Query(value = "UPDATE Student SET course = :newCourse WHERE user_id = :id", nativeQuery = true)
    @Transactional
    void updateStudentCourse(@Param("id") String id, @Param("newCourse") String newCourse);

    @Modifying
    @Query(value = "UPDATE Student SET classes = :newClasses WHERE user_id = :id", nativeQuery = true)
    @Transactional
    void updateStudentClasses(@Param("id") String id, @Param("newClasses") String[] newClasses);

    // Atualiza as aulas do estudante na tabela respectiva
    @Modifying
    @Query(value = "UPDATE student_classes SET classes = :newClasses WHERE student_user_id = :id", nativeQuery = true)
    @Transactional
    void updateStudentClassesTable(@Param("id") String id, @Param("newClasses") String[] newClasses);
}
