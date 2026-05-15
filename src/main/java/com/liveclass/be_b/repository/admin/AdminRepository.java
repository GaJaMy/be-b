package com.liveclass.be_b.repository.admin;

import com.liveclass.be_b.domain.admin.entity.Admin;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AdminRepository extends JpaRepository<Admin, String> {
    Optional<Admin> findByLoginId(String loginId);
}
