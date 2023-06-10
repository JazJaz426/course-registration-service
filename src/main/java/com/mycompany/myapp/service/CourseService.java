package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.Course;
import com.mycompany.myapp.domain.User;
import com.mycompany.myapp.domain.UserCourse;
import com.mycompany.myapp.repository.CourseRepository;
import com.mycompany.myapp.repository.UserCourseRepository;
import com.mycompany.myapp.repository.UserRepository;
import com.mycompany.myapp.service.dto.CourseDTO;
import com.mycompany.myapp.service.mapper.CourseMapper;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CourseService {

    private CourseRepository courseRepository;
    private CourseMapper courseMapper;
    private UserRepository userRepository;
    private UserCourseRepository userCourseRepository;

    /**
     * 1. User exists -> get User by userName
     * 2. Course exists -> get Course by courseName
     * 3. new UserCourse(user, course)
     * 4. Check if UserCourse not exists (不要重复选课）
     * 5. save this new UserCourse -> save entity to user_course table
     * @param userName
     * @param courseName
     */
    public void enrollCourse(String userName, String courseName) {
        // 1. get User by userName
        User user = getUserByUserName(userName);
        // 2. get Course by courseName
        Course course = getCourseByCourseName(courseName);
        // 3. new UserCourse
        UserCourse userCourse = new UserCourse(user, course);
        // 4. Check if UserCourse not exists (不要重复选课）
        Optional<UserCourse> optionalUserCourse = this.userCourseRepository.findOneByUserAndCourse(user, course);
        optionalUserCourse.ifPresent(
            existingUserCourse -> {
                throw new IllegalArgumentException("UserCourse already exists: " + existingUserCourse.toString());
            }
        );
        // 5. save this new UserCourse
        userCourseRepository.save(userCourse);
    }

    public List<CourseDTO> listAllCourses() {
        return this.courseMapper.coursesToCourseDTOs(this.courseRepository.findAll());
    }

    /**
     * 1. User exists -> get User by userName
     * 2. Get List<UserCourse> based on User
     * 3. Get List<Course> from List<UserCourse>
     * 4. List<Course> -> List<CourseDTO>
     * @param userName
     * @return
     */
    public List<CourseDTO> getEnrolledCourses(String userName) {
        // 1. get User by userName
        User user = getUserByUserName(userName);
        // 2. Get List<UserCourse> based on User
        List<UserCourse> userCourses = this.userCourseRepository.findAllByUser(user);
        // 3. Get List<Course> from List<UserCourse>
        List<Course> courses = new ArrayList<>();
        for (UserCourse userCourse : userCourses) {
            courses.add(userCourse.getCourse());
        }
        // 4. List<Course> -> List<CourseDTO>
        return this.courseMapper.coursesToCourseDTOs(courses);
    }

    /**
     * 1. User exists -> get User by userName
     * 2. Course exists -> get Course by courseName
     * 3. remove this UserCourse by User and Course
     * @param userName
     * @param courseName
     */
    public void dropCourse(String userName, String courseName) {
        // 1. get User by userName
        User user = getUserByUserName(userName);
        // 2. get Course by courseName
        Course course = getCourseByCourseName(courseName);
        // 3. remove this UserCourse
        this.userCourseRepository.deleteByUserAndCourse(user, course);
    }

    private User getUserByUserName(String userName) {
        Optional<User> optionalUser = this.userRepository.findOneByLogin(userName);
        return optionalUser.orElseThrow(() -> new UsernameNotFoundException("No such user: " + userName));
    }

    private Course getCourseByCourseName(String courseName) {
        Optional<Course> optionalCourse = this.courseRepository.findOneByCourseName(courseName);
        return optionalCourse.orElseThrow(() -> new IllegalArgumentException("No such course: " + courseName));
    }
}
