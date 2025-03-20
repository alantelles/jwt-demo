package com.alangomes.jwtdemo.dto

import io.micronaut.core.annotation.Introspected
import io.micronaut.serde.annotation.Serdeable
import jakarta.validation.constraints.NotBlank

@Introspected
@Serdeable
class ServiceCredentialsDto extends AppUser {

    @NotBlank
    String clientId

    @NotBlank
    String secret

    static ServiceCredentialsDto build(String clientId, List<String> roles) {
        return new ServiceCredentialsDto(
                clientId: clientId,
                roles: roles
        )
    }

}
