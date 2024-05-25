package com.example.tastyhub.annotation;


import com.example.tastyhub.common.domain.user.entity.User.userType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import org.springframework.security.test.context.support.WithSecurityContext;

@Retention(RetentionPolicy.RUNTIME)
@WithSecurityContext(factory = WithMockCustomAdminSecurityContextFactory.class)
public @interface WithCustomMockAdmin {


    String username() default "username1";

    String password() default "Password1!";

    String nickname() default "nickname";

    userType role() default userType.ADMIN;
}
