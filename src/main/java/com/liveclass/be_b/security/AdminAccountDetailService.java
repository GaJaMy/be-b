package com.liveclass.be_b.security;

import com.liveclass.be_b.domain.admin.entity.Admin;
import com.liveclass.be_b.repository.admin.AdminRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminAccountDetailService implements UserDetailsService {

    private final AdminRepository adminRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Admin admin = adminRepository.findByLoginId(username)
                .orElseThrow(() -> new UsernameNotFoundException("Not Found Admin" + username));
        return new AuthenticatedPrincipal(
                admin.getLoginId(),
                admin.getPasswordHash(),
                admin.getId(),
                AuthenticatedPrincipal.ADMIN_ROLE,
                AuthenticatedPrincipal.ADMIN_PRINCIPAL_TYPE
        );
    }
}
