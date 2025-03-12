//file:noinspection GrMethodMayBeStatic
package com.alangomes.jwtdemo

import io.micronaut.http.annotation.Body
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Post
import io.micronaut.security.annotation.Secured
import io.micronaut.security.rules.SecurityRule
import jakarta.validation.Valid

@Controller
class AppController {

//    @Secured(SecurityRule.IS_ANONYMOUS)
//    @Post('/login')
//    Map<String, Object> login(@Valid @Body LoginCommand command) {
//        if (JwtDemoAccessTokenGenerator.isAuthorized(command)) {
//            return [msg: "Hello!"]
//        }
//        return [msg: "Hello!"]
//    }

}
