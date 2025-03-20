package com.alangomes.jwtdemo

import com.alangomes.jwtdemo.dto.LoginDto
import com.alangomes.jwtdemo.dto.ServiceCredentialsDto
import io.micronaut.context.annotation.Value
import io.micronaut.core.annotation.NonNull
import io.micronaut.core.annotation.Nullable
import io.micronaut.http.HttpRequest
import io.micronaut.security.authentication.Authentication
import io.micronaut.security.authentication.AuthenticationRequest
import io.micronaut.security.authentication.AuthenticationResponse
import io.micronaut.security.authentication.UsernamePasswordCredentials
import io.micronaut.security.authentication.provider.HttpRequestAuthenticationProvider
import io.micronaut.security.token.jwt.generator.JwtTokenGenerator
import io.micronaut.security.token.render.BearerAccessRefreshToken
import jakarta.inject.Inject
import jakarta.inject.Singleton

import static com.alangomes.jwtdemo.dto.LoginDto.build

@Singleton
class LoginService implements HttpRequestAuthenticationProvider<UsernamePasswordCredentials> {

    final static Integer DEFAULT_SERVICE_ACCESS_EXPIRES_IN = 1200
    final static String TOKEN_TYPE = 'Bearer'

    final static List<LoginDto> CREDENTIALS = [
            build('usuarioadmin', 'admin', ['ADMIN']),
            build('usuarioupdater', 'updater', ['UPDATER']),
            build('usuarioviewer', 'viewer', ['VIEWER']),
    ]

    final static List<ServiceCredentialsDto> SERVICE_CREDENTIALS = [
            ServiceCredentialsDto.build('updater-app', ['UPDATER']),
    ]

    @Value('${micronaut.security.token.jwt.signatures.secret.generator.secret}')
    String secret

    @Inject
    JwtTokenGenerator jwtTokenGenerator

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

    BearerAccessRefreshToken authenticateService(ServiceCredentialsDto credentials) {
        if (credentials.secret != secret) {
            return null
        }
        ServiceCredentialsDto authed = SERVICE_CREDENTIALS.find { it.clientId == credentials.clientId }
        if (!authed) {
            return null
        }
        String token = jwtTokenGenerator.generateToken(Authentication.build(
                credentials.clientId,
                authed.roles,
                [isService: true]), DEFAULT_SERVICE_ACCESS_EXPIRES_IN).get()
        return new BearerAccessRefreshToken(
                credentials.clientId, authed.roles, DEFAULT_SERVICE_ACCESS_EXPIRES_IN,
                token, null, TOKEN_TYPE
        )
    }

}
