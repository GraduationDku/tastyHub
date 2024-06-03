package com.example.tastyhub.annotation;

import com.example.tastyhub.common.domain.user.entity.User;
import com.example.tastyhub.common.utils.Jwt.UserDetailsImpl;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

public class WithMockCustomAdminSecurityContextFactory implements
    WithSecurityContextFactory<WithCustomMockAdmin> {

    @Override
    public SecurityContext createSecurityContext(WithCustomMockAdmin annotation) {
        final SecurityContext context = SecurityContextHolder.createEmptyContext();
        User user = User.builder().username(annotation.username()).userType(annotation.role())
            .password(
                annotation.password()).nickname(annotation.nickname()).build();
        UserDetailsImpl userDetails = new UserDetailsImpl(user);
        final Authentication authentication = new UsernamePasswordAuthenticationToken(
            userDetails, user.getPassword(),
            userDetails.getAuthorities());

        context.setAuthentication(authentication);
        return context;
    }
}
