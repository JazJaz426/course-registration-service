package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Course;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CourseRepository extends JpaRepository<Course/*ORM model class*/, Long/*primary key data type*/> {
    Optional<Course> findOneByCourseName(String courseName);
}
