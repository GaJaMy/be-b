package com.liveclass.be_b.repository.course;

import com.liveclass.be_b.domain.course.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface CourseRepository extends JpaRepository<Course, String> {

    @Query("""
      select c
      from Course c
      join fetch c.creator
      where c.id = :courseId
    """)
    Optional<Course> findByIdWithCreator(@Param("courseId") String courseId);
}
