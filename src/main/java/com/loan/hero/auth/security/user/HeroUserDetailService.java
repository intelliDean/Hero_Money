package com.loan.hero.auth.security.user;
import com.loan.hero.auth.user.data.models.User;
import com.loan.hero.auth.user.service.UserService;
import lombok.AllArgsConstructor;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class HeroUserDetailService implements UserDetailsService {
    private final UserService userService;
    @Override
    public UserDetails loadUserByUsername(
            String username
    ) throws UsernameNotFoundException {
        final User user = userService.findUserByEmail(username);
        return new AuthenticatedUser(user);
    }
}
