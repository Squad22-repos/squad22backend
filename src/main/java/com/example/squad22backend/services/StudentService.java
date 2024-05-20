package com.example.squad22backend.services;

import com.example.squad22backend.repositories.StudentRepository;
import org.springframework.stereotype.Service;

@Service
public class StudentService {

    private final StudentRepository studentRepository;

    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    public void updateStudentData(String id, String newCourse, String[] newClasses) {
        if (newCourse != null && !newCourse.isEmpty()) {
            this.studentRepository.updateStudentCourse(id, newCourse);
        }
        if (newClasses != null) {
            this.studentRepository.updateStudentClasses(id, newClasses);
            this.studentRepository.updateStudentClassesTable(id, newClasses);
        }
    }
}
