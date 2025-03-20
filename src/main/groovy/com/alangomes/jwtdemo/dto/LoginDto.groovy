package com.alangomes.jwtdemo.dto

import io.micronaut.core.annotation.Introspected
import io.micronaut.serde.annotation.Serdeable
import jakarta.validation.constraints.NotBlank

@Serdeable
@Introspected
class LoginDto extends AppUser {

    @NotBlank
    String username

    @NotBlank
    String password

    static LoginDto build(String username, String password, List<String> roles) {
        return new LoginDto(
                username: username,
                password: password,
                roles: roles
        )
    }

}
