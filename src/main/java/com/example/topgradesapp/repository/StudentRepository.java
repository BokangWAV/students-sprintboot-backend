package com.example.topgradesapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.topgradesapp.model.Student;

import java.util.List;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
    List<Student> findByScore(int score);
    List<Student> findAllByOrderByScoreDesc();
    
}