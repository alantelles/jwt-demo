package com.alangomes.jwtdemo

import io.micronaut.core.annotation.NonNull
import io.micronaut.core.annotation.Nullable
import io.micronaut.http.HttpRequest
import io.micronaut.security.authentication.AuthenticationRequest
import io.micronaut.security.authentication.AuthenticationResponse
import io.micronaut.security.authentication.UsernamePasswordCredentials
import io.micronaut.security.authentication.provider.HttpRequestAuthenticationProvider
import jakarta.inject.Singleton

import static com.alangomes.jwtdemo.LoginDto.build

@Singleton
class LoginService implements HttpRequestAuthenticationProvider<UsernamePasswordCredentials> {

    final static List<LoginDto> CREDENTIALS = [
            build('usuarioadmin', 'admin', ['ADMIN']),
            build('usuarioupdater', 'updater', ['UPDATER']),
            build('usuarioviewer', 'viewer', ['VIEWER']),
    ]

    @Override
    AuthenticationResponse authenticate(@NonNull AuthenticationRequest<String, String> authRequest) {
        return AuthenticationResponse.success(authRequest.identity)
    }

    @Override
    AuthenticationResponse authenticate(@Nullable HttpRequest<UsernamePasswordCredentials> requestContext, @NonNull AuthenticationRequest<String, String> authRequest) {
        UsernamePasswordCredentials credentials = requestContext.body.get()
        LoginDto authed = CREDENTIALS.find {
            it.username == credentials.username && it.password == credentials.password
        }
        if (!authed) {
            throw new IllegalAccessException("Invalid credentials")
        }
        return AuthenticationResponse.success(credentials.username, authed.roles)
    }
}
