package com.liveclass.be_b.security;

import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class CreatorAuthenticationProvider extends DaoAuthenticationProvider {

    public CreatorAuthenticationProvider(
            CreatorAccountDetailService creatorAccountDetailService,
            PasswordEncoder passwordEncoder
    ) {
        super(creatorAccountDetailService);
        setPasswordEncoder(passwordEncoder);
    }
}
