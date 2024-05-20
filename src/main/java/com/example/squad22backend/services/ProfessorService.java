package com.example.squad22backend.services;

import com.example.squad22backend.repositories.ProfessorRepository;
import org.springframework.stereotype.Service;

@Service
public class ProfessorService {

    private final ProfessorRepository professorRepository;

    public ProfessorService(ProfessorRepository professorRepository) {
        this.professorRepository = professorRepository;
    }

    public void updateProfessorData(String id, String[] newCourses, String[] newClasses, String[] newDegrees) {
        if (newCourses != null && newCourses.length > 0) {
            this.professorRepository.updateProfessorCourses(id, newCourses);
            this.professorRepository.updateProfessorCoursesTable(id, newCourses);
        }
        if (newClasses != null && newClasses.length > 0) {
            this.professorRepository.updateProfessorClasses(id, newClasses);
            this.professorRepository.updateProfessorClassesTable(id, newClasses);
        }
        if (newDegrees != null && newDegrees.length > 0) {
            this.professorRepository.updateProfessorDegrees(id, newDegrees);
            this.professorRepository.updateProfessorDegreesTable(id, newDegrees);
        }
    }
}
