package com.atharva.educationcrm.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.atharva.educationcrm.entity.Course;
import com.atharva.educationcrm.entity.Course.CourseStatus;
import com.atharva.educationcrm.exception.ResourceNotFoundException;
import com.atharva.educationcrm.repository.CourseRepository;
import com.atharva.educationcrm.service.CourseService;

@Service
@Transactional(readOnly = true)
public class CourseServiceImpl implements CourseService {

    private final CourseRepository courseRepository;

    public CourseServiceImpl(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    @Override
    @Transactional
    public Course addCourse(Course course) {
        return courseRepository.save(course);
    }

    @Override
    @Transactional
    public Course updateCourse(Course submitted) {
        Course course = getCourseById(submitted.getId());
        course.setTitle(submitted.getTitle().trim());
        course.setCategory(submitted.getCategory().trim());
        course.setDescription(submitted.getDescription().trim());
        course.setPrice(submitted.getPrice());
        course.setDuration(submitted.getDuration());
        course.setLevel(submitted.getLevel());
        course.setImageUrl(submitted.getImageUrl());
        course.setStatus(submitted.getStatus());
        return courseRepository.save(course);
    }

    @Override
    public List<Course> getAllCourses() {
        return courseRepository.findAll();
    }

    @Override
    public List<Course> getActiveCourses() {
        return courseRepository.findByStatusOrderByCreatedAtDesc(CourseStatus.ACTIVE);
    }

    @Override
    public List<Course> searchCourses(String keyword) {
        if (keyword == null || keyword.isBlank()) {
            return getAllCourses();
        }
        String value = keyword.trim();
        return courseRepository.findByTitleContainingIgnoreCaseOrCategoryContainingIgnoreCase(value, value);
    }

    @Override
    public Course getCourseById(Long id) {
        return courseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found with id " + id));
    }

    @Override
    @Transactional
    public void deactivateCourse(Long id) {
        Course course = getCourseById(id);
        course.setStatus(CourseStatus.INACTIVE);
        courseRepository.save(course);
    }
}
