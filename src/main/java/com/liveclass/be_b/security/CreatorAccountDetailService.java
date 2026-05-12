package com.liveclass.be_b.security;

import com.liveclass.be_b.domain.creator.entity.Creator;
import com.liveclass.be_b.repository.creator.CreatorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CreatorAccountDetailService implements UserDetailsService {

    private final CreatorRepository creatorRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Creator creator = creatorRepository.findByLoginId(username)
                .orElseThrow(() -> new UsernameNotFoundException("Not Found Creator" + username));

        return new AuthenticatedPrincipal(
                creator.getLoginId(),
                creator.getPasswordHash(),
                creator.getId(),
                AuthenticatedPrincipal.CREATOR_ROLE,
                AuthenticatedPrincipal.CREATOR_PRINCIPAL_TYPE
        );
    }
}
