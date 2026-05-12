package com.liveclass.be_b.repository.creator;

import com.liveclass.be_b.domain.creator.entity.Creator;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CreatorRepository extends JpaRepository<Creator, String> {
    Optional<Creator> findByLoginId(String loginId);
}
