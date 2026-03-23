package com.example.topgradesapp.model;

import jakarta.persistence.*;

@Entity
@Table(name = "students")

public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String firstName;
    private String secondName;
    private int score;

    public Student() {}

    public Student(String firstName, String secondName, int score) {
        this.firstName = firstName;
        this.secondName = secondName;
        this.score = score;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getSecondName() {
        return secondName;
    }

    public int getScore() {
        return score;
    }

    public String getFullName() {
        return firstName + " " + secondName;
    }

    @Override
    public String toString() {
        return firstName + " " + secondName + " - Score: " + score;
    }
}
