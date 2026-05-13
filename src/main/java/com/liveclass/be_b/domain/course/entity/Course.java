package com.liveclass.be_b.domain.course.entity;

import com.liveclass.be_b.common.domain.BaseEntity;
import com.liveclass.be_b.domain.creator.entity.Creator;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "course")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Course extends BaseEntity {

    @Id
    @Column(name = "id", length = 50, nullable = false)
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "creator_id", nullable = false, foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Creator creator;

    @Column(name = "title", length = 200, nullable = false)
    private String title;

    public static Course create(String id, Creator creator, String title) {
        Course course = new Course();

        course.id = id;
        course.creator = creator;
        course.title = title;

        return course;
    }
}
