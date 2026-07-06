package com.api.xpress.auth.security.user;

import com.api.xpress.auth.user.data.models.User;
import com.api.xpress.auth.user.service.UserService;
import com.api.xpress.exceptions.UserNotFoundException;
import lombok.AllArgsConstructor;

import org.jspecify.annotations.NonNull;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class XpressUserDetailService implements UserDetailsService {

    private final UserService userService;

    public @NonNull UserDetails loadUserByUsername(@NonNull String emailAddress) throws UsernameNotFoundException {
        try {
            User user = userService.findUserByEmail(emailAddress);

            return new AuthenticatedUser(user);

        } catch (UserNotFoundException e) {
            throw new UsernameNotFoundException("Invalid email or password", e);
        }
    }
}
