package com.example.topgradesapp.service;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.topgradesapp.model.Student;
import com.example.topgradesapp.repository.StudentRepository;

// * Transactional used for: DELETE  & UPDATE

@Service
public class StudentService {

    @Autowired
    private StudentRepository studentRepository;

    @Transactional
    public void deleteStudent(String firstName, String secondName) {
        studentRepository.deleteByFirstNameAndSecondName(firstName, secondName);
    }

    public int updateStudentScore(String firstName, String secondName, int score) {
        return studentRepository.updateScore(firstName, secondName, score);
    }

    public Student saveStudent(Student student) {
        return studentRepository.save(student);
    }

    public List<Student> saveStudents(List<Student> students) {
        return studentRepository.saveAll(students);
    }

    public List<Student> getStudent(String firstName, String secondName) {
        return studentRepository.findByFirstNameAndSecondName(firstName, secondName);
    }

    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    public List<Student> getAllStudentsSorted() {
        return studentRepository.findAllByOrderByScoreDesc();
    }

    public List<Student> getTopScoreStudents() {
        List<Student> sorted = getAllStudentsSorted();
        if (sorted.isEmpty()) return List.of();

        int topScore = sorted.get(0).getScore();
        return studentRepository.findByScore(topScore);
    }
    

    public Student parseStudent(String studentsCSV) {
        String[] students = studentsCSV.split(",");
        if (students.length != 3) {
            throw new IllegalArgumentException(
                "Insufficient Information: must be First Name, Second Name & Score"
            );
        }

        int score;
        try {
            score = Integer.parseInt(students[2].trim());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid Score Format: Please Provide A Valid Score");
        }

        return new Student(students[0].trim(), students[1].trim(), score);
    }

    public List<Student> storeStudents(String studentsCSV) {
        List<Student> allStudents = new ArrayList<>();
        String[] lines = studentsCSV.split("\\r?\\n");

        for (int i = 1; i < lines.length; i++) {
            if (!lines[i].trim().isEmpty()) { 
                allStudents.add(parseStudent(lines[i]));
            }
        }

        return allStudents;
    }

    public HashMap<Integer, List<Student>> getStudentsWithHighestScore(String studentsCSV) {
        List<Student> students = storeStudents(studentsCSV);
        if (students.isEmpty()) {
            return new HashMap<>();
        }

        // Sort descending by score
        students.sort(Comparator.comparingInt(Student::getScore).reversed());

        int maxScore = students.get(0).getScore();

        List<Student> topStudents = new ArrayList<>();

        for (Student s : students) {
            if (s.getScore() == maxScore) {
                topStudents.add(s);
            }
        }

        //Sort topStudents alphectically (By both first and Secondname)
        topStudents.sort(Comparator.comparing(Student::getFullName));

        HashMap<Integer, List<Student>> scoreMap = new HashMap<>();
        scoreMap.put(maxScore, topStudents);
        return scoreMap;

    }
}