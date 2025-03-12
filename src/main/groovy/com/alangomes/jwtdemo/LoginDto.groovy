package com.alangomes.jwtdemo

import io.micronaut.core.annotation.Introspected
import io.micronaut.serde.annotation.Serdeable
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Null

@Serdeable
@Introspected
class LoginDto {

    @NotBlank
    String username

    @NotBlank
    String password

    @Null
    List<String> roles

    static LoginDto build(String username, String password, List<String> roles) {
        return new LoginDto(
                username: username,
                password: password,
                roles: roles
        )
    }

}
