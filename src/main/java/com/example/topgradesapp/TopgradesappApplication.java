package com.example.topgradesapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.topgradesapp.model.Student;
import org.springframework.web.multipart.MultipartFile;
import com.example.topgradesapp.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.*;



@SpringBootApplication
@RestController
@RequestMapping("/students")

// what were doing:
// uploading a file (CSV) that we then take that data and store it in Student object
// StudentService provides functions such as parseStudent: create Student object, storeStudents: ArrayList of all students provided by file and get top student


public class TopgradesappApplication {

    @Autowired
    private StudentService studentService;

    public static void main(String[] args) {
        SpringApplication.run(TopgradesappApplication.class, args);
    }

	@PostMapping("/uploadStudentCSV")
	public List<Student> uploadCSVToDB(@RequestParam("file") MultipartFile file) {
		try(BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
			StringBuilder csvBuilder = new StringBuilder();
			String line;
			while ((line = reader.readLine()) != null) {
				csvBuilder.append(line).append("\n");
			}

			List<Student> students = studentService.storeStudents(csvBuilder.toString());
			return studentService.saveStudents(students);
		} catch (Exception e) {
			throw new RuntimeException("Failed to read CSV file: " + e.getMessage() );
		}
	}

	@PostMapping("/uploadStudentCSVAndGetTop")
	public List<Student> uploadStudentCSVAndGetTop(@RequestParam("file") MultipartFile file) {
		try(BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
			StringBuilder csvBuilder = new StringBuilder();
			String line;
			while ((line = reader.readLine()) != null) {
				csvBuilder.append(line).append("\n");
			}

        	List<Student> students = studentService.storeStudents(csvBuilder.toString());

        	studentService.saveStudents(students);

			HashMap<Integer, List<Student>> topMap = studentService.getStudentsWithHighestScore(csvBuilder.toString());
        	return topMap.values().stream().findFirst().orElse(List.of());
		} catch (Exception e) {
			throw new RuntimeException("Failed to read CSV file: " + e.getMessage() );
		}
	}

	@PostMapping("/addStudent")
	public List<Student> uploadIndividualStudent(@RequestParam("firstname") String firstName, @RequestParam("secondname") String secondName, @RequestParam("score") int score) {
		try {
			Student newStudent = new Student(firstName, secondName, score);
			studentService.saveStudent(newStudent);
			return studentService.getAllStudents();
		}
		catch (Exception e) {
			throw new RuntimeException("Failed to read student record: " + e.getMessage());
		}
	}

	@GetMapping("/fetchStudent")
	public List<Student> getStudent(@RequestParam("firstname") String firstName, @RequestParam ("secondname") String secondName) {
		try {
			List<Student> student = studentService.getStudent(firstName, secondName);
			return student;
		}
		catch (Exception e) {
			throw new RuntimeException("Failed to fetch student from database: " + e.getMessage());
		}
	}

    @GetMapping("/fetchAllStudents") 
	public List<Student> getAllStudents() {

		try {
			List<Student> allStudents = studentService.getAllStudents();
			return allStudents;
		}
		catch (Exception e) {
			throw new RuntimeException("Failed to fetch all students from database: " + e.getMessage());
		}
	}



	@GetMapping("/fetchTopStudents")
	public List<Student> getTopStudents() {
		try {
			List<Student> topStudents = studentService.getTopScoreStudents();
			return topStudents;
		}
		catch(Exception e) {
			throw new RuntimeException("Failed to fetch top students from database: " + e.getMessage());
		}
	}

	@DeleteMapping("/deleteStudent") 
	public ResponseEntity<String> deleteStudent(@RequestParam("firstname") String firstName, @RequestParam("secondname") String secondName) {
		List<Student> student = studentService.getStudent(firstName, secondName);
		if(student.isEmpty()) {
			return ResponseEntity.status(404).body("Student not found");
		}
		try {
			studentService.deleteStudent(firstName, secondName);
			return ResponseEntity.ok("Deleted");
		}
		catch (Exception e) {
			throw new RuntimeException("Failed to delete student from database: " + e.getMessage());
		}
	}

	@PutMapping("/updateScore")
	public ResponseEntity<String> updateStudentScore(@RequestParam("firstname") String firstName, @RequestParam("secondname") String secondName, @RequestParam("score") int score) {

		int updated = studentService.updateStudentScore(firstName, secondName, score);

		if(updated == 0) {
			return ResponseEntity.status(404).body("Student not found");
		}

		return ResponseEntity.ok("Updated " + updated + " student(s)");
	}
}