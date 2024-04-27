package com.springmvcapp.model;
public class Course {
    private String courseId;
    private String courseName;

    // Constructor
    public Course(String courseId, String courseName) {
        this.courseId = courseId;
        this.courseName = courseName;
    }

    // Getters
    public String getCourseId() {
        return courseId;
    }

    public String getCourseName() {
        return courseName;
    }

    // Setters if necessary
}
