package com.alangomes.jwtdemo

import io.micronaut.core.annotation.Introspected
import io.micronaut.serde.annotation.Serdeable
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull

@Serdeable
@Introspected
class LoginCommand {

    @NotBlank
    String username

    @NotBlank
    String password

}
