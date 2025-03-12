package com.alangomes.jwtdemo

import io.micronaut.core.annotation.NonNull
import io.micronaut.core.annotation.Nullable
import io.micronaut.http.HttpRequest
import io.micronaut.security.authentication.Authentication
import io.micronaut.security.authentication.AuthenticationRequest
import io.micronaut.security.authentication.AuthenticationResponse
import io.micronaut.security.authentication.UsernamePasswordCredentials
import io.micronaut.security.authentication.provider.HttpRequestAuthenticationProvider
import jakarta.inject.Singleton

@Singleton
class LoginService implements HttpRequestAuthenticationProvider<UsernamePasswordCredentials> {

    final static String USERNAME = 'algumusuario'
    final static String PASSWORD = 'algumasenha'

    private static Authentication authorize(LoginCommand command) {
        boolean authed = command.username == USERNAME && command.password == PASSWORD
        if (!authed) {
            throw new IllegalAccessException("Invalid credentials")
        }
        return Authentication.build(command.username)
    }

    @Override
    AuthenticationResponse authenticate(@NonNull AuthenticationRequest<String, String> authRequest) {
        return AuthenticationResponse.success(authRequest.identity)
    }

    @Override
    AuthenticationResponse authenticate(@Nullable HttpRequest<UsernamePasswordCredentials> requestContext, @NonNull AuthenticationRequest<String, String> authRequest) {
        UsernamePasswordCredentials credentials = requestContext.body.get()
        boolean authed = credentials.username == USERNAME && credentials.password == PASSWORD
        if (!authed) {
            throw new IllegalAccessException("Invalid credentials")
        }
        return AuthenticationResponse.success(credentials.username)
    }
}
