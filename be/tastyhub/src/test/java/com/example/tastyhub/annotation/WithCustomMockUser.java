package com.example.tastyhub.annotation;


import static com.example.tastyhub.common.domain.user.entity.User.userType.*;

import com.example.tastyhub.common.domain.user.entity.User.userType;
import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import org.springframework.security.test.context.support.WithSecurityContext;

@Retention(RetentionPolicy.RUNTIME)
@WithSecurityContext(factory = WithMockCustomUserSecurityContextFactory.class)
public @interface WithCustomMockUser {


    String username() default "username1";

    String password() default "Password1!";

    String nickname() default "nickname";

    userType role() default userType.COMMON;
}
