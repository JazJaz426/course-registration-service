package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.security.SecurityUtils;
import com.mycompany.myapp.service.CourseService;
import com.mycompany.myapp.service.dto.CourseDTO;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/api")
public class CourseController {

    private CourseService courseService;

    /**
     * 列出所有已选课程
     * 1. Http Method: GET
     * 2. URL: /allCourses
     * 3. Http Status Code: 200
     * 4. Request Body（前端需要给后端提供什么东西吗？) : No
     * 5. Response Body (后端需要返回给前端什么东西吗？）- List<CourseDTO>
     * 6. Request Header (JWT Token)
     * http request -> Java Method 一一映射 叫做API
     */
    @GetMapping(path = "/allCourses")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public List<CourseDTO> listAllCourses() {
        return courseService.listAllCourses();
    }

    /**
     * 实现学生选课功能 (创建一种关系 学生-课程）
     * 1. Http Method: 增 - POST
     * 2. URL: /student/course/{courseName}
     * 3. Http Status Code: 200, 201 (创建成功）
     * 4. Request Body（前端需要给后端提供什么东西吗？） - {courseName} - URL Parameter
     * 5. Response Body (后端需要返回给前端什么东西吗？）- None
     * 6. Request Header (JWT Token)
     * http request -> Java Method 一一映射 叫做API
     */
    @PostMapping(path = "/student/course/{courseName}")
    @ResponseStatus(HttpStatus.CREATED)
    public void enrollCourse(@PathVariable String courseName) {
        // 1. get userName by JWT Token
        String userName = getUserName();
        // 2. enroll course with userName + courseName
        courseService.enrollCourse(userName, courseName);
    }

    /**
     * 列出学生已选课程
     * 1. Http Method: GET
     * 2. URL: /student/enrolledCourses
     * 3. Http Status: 200
     * 4. Request Body: No
     * 5. Response Body: List<CourseDTO>
     * 6. Request Header: JWT token (要去你登陆）
     */
    @GetMapping(path = "/student/enrolledCourses")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public List<CourseDTO> getEnrolledCourses() {
        // 1. get userName by JWT Token
        String userName = getUserName();
        return courseService.getEnrolledCourses(userName);
    }

    /**
     * 实现drop课的功能
     * 1. Http Method: 删 - DELETE
     * 2. URL: /student/course/{courseName}
     * 3. Http Status Code: 200, 204 (no content)
     * 4. Request Body（前端需要给后端提供什么东西吗？） - {courseName} - URL Parameter
     * 5. Response Body (后端需要返回给前端什么东西吗？）- None
     * 6. Request Header (JWT Token)
     */
    @DeleteMapping(path = "/student/course/{courseName}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void dropCourse(@PathVariable String courseName) {
        // 1. get userName by JWT Token
        String userName = getUserName();
        // 2. drop course with userName + courseName
        courseService.dropCourse(userName, courseName);
    }

    /**
     * Extract userName from JWT token
     */
    private String getUserName() {
        return SecurityUtils
            .getCurrentUserLogin()
            .orElseThrow(
                () -> {
                    throw new UsernameNotFoundException("Username not found");
                }
            );
    }
}
