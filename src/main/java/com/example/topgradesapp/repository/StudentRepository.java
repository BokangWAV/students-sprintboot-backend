package com.example.topgradesapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.example.topgradesapp.model.Student;

import java.util.List;

// * updateScore: doest not return "score that was updated" but will return number of entries affected/changed by query

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
    List<Student> findByScore(int score);
    List<Student> findAllByOrderByScoreDesc();

    List<Student> findByFirstNameAndSecondName(String firstName, String secondName);

    void deleteByFirstNameAndSecondName(String firstName, String secondName);
    
    @Modifying
    @Transactional
    @Query("""
    UPDATE Student s
    SET s.score = :score
    WHERE s.firstName = :firstName
    AND s.secondName = :secondName
    """)
    int updateScore(String firstName, String secondName, int score);
}