package com.mycompany.myapp.service.mapper;

import com.mycompany.myapp.domain.Course;
import com.mycompany.myapp.service.dto.CourseDTO;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

/**
 * Convert entity(model) class Course to CourseDTO (data transfer object)
 */
@Service
public class CourseMapper {

    public CourseDTO courseToCourseDTO(Course course) {
        return CourseDTO
            .builder()
            .courseName(course.getCourseName())
            .courseContent(course.getCourseContent())
            .courseLocation(course.getCourseLocation())
            .teacherId(course.getTeacherId())
            .build();
    }

    public List<CourseDTO> coursesToCourseDTOs(List<Course> courses) {
        return courses.stream().filter(Objects::nonNull).map(this::courseToCourseDTO).collect(Collectors.toList());
    }
}
