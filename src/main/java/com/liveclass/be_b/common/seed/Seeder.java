package com.liveclass.be_b.common.seed;

import com.liveclass.be_b.domain.admin.entity.Admin;
import com.liveclass.be_b.domain.creator.entity.Creator;
import com.liveclass.be_b.repository.admin.AdminRepository;
import com.liveclass.be_b.repository.creator.CreatorRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class Seeder {
    private static final String RAW_PASSWORD = "qwe123123!";

    @Bean
    @ConditionalOnProperty(prefix = "app.seed", name = "activate", havingValue = "true")
    CommandLineRunner seedCreate(
            PasswordEncoder passwordEncoder,
            AdminRepository adminRepository,
            CreatorRepository creatorRepository
    ) {
        return createSeedRunner(
                passwordEncoder,
                adminRepository,
                creatorRepository
        );
    }

    private CommandLineRunner createSeedRunner(
            PasswordEncoder passwordEncoder,
            AdminRepository adminRepository,
            CreatorRepository creatorRepository
    ) {
        return args -> doSeed(
            passwordEncoder,
            adminRepository,
            creatorRepository
        );
    }

    @Transactional
    void doSeed(
            PasswordEncoder passwordEncoder,
            AdminRepository adminRepository,
            CreatorRepository creatorRepository
    ) {
        String hashedPassword = passwordEncoder.encode(RAW_PASSWORD);
        List<Creator> creatorList = createCreator(hashedPassword);
        creatorRepository.saveAll(creatorList);

        List<Admin> adminList = createAdmin(hashedPassword);
        adminRepository.saveAll(adminList);
    }

    private List<Creator> createCreator(String hashedPassword) {
        List<Creator> creatorList = new ArrayList<>();
        creatorList.add(Creator.create("creator-1", "creator-1", hashedPassword, "김강사"));
        creatorList.add(Creator.create("creator-2", "creator-2", hashedPassword, "이강사"));
        creatorList.add(Creator.create("creator-3", "creator-3", hashedPassword, "박강사"));
        return creatorList;
    }

    private List<Admin> createAdmin(String hashedPassword) {
        List<Admin> adminList = new ArrayList<>();
        adminList.add(Admin.create("admin-1", "admin-1", hashedPassword, "서관리자"));
        return adminList;
    }
}
