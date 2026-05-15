package com.liveclass.be_b.security;

import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class AdminAuthenticationProvider extends DaoAuthenticationProvider {

    public AdminAuthenticationProvider(
            AdminAccountDetailService adminAccountDetailService,
            PasswordEncoder passwordEncoder
    ) {
        super(adminAccountDetailService);
        setPasswordEncoder(passwordEncoder);
    }
}
