package com.liveclass.be_b.domain.admin.entity;

import com.liveclass.be_b.common.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "admin")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Admin extends BaseEntity {

    @Id
    @Column(name = "id", length = 50, nullable = false)
    private String id;

    @Column(name = "login_id", length = 100, unique = true, nullable = false)
    private String loginId;

    @Column(name = "password_hash", length = 255, nullable = false)
    private String passwordHash;

    @Column(name = "name", length = 100, nullable = false)
    private String name;

    public static Admin create(String id, String loginId, String passwordHash, String name) {
        Admin admin = new Admin();

        admin.id = id;
        admin.loginId = loginId;
        admin.passwordHash = passwordHash;
        admin.name = name;

        return admin;
    }
}
