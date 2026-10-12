package com.atharva.educationcrm.service;

import java.util.List;

import com.atharva.educationcrm.entity.Course;

public interface CourseService {
    Course addCourse(Course course);
    Course updateCourse(Course course);
    List<Course> getAllCourses();
    List<Course> getActiveCourses();
    List<Course> searchCourses(String keyword);
    Course getCourseById(Long id);
    void deactivateCourse(Long id);
}
