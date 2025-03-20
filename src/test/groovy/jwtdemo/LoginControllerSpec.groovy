package jwtdemo

import com.fasterxml.jackson.databind.ObjectMapper
import com.nimbusds.jwt.JWT
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpResponse
import io.micronaut.http.HttpStatus
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.security.token.jwt.generator.JwtTokenGenerator
import io.micronaut.security.token.jwt.validator.JsonWebTokenValidator
import io.micronaut.test.extensions.spock.annotation.MicronautTest
import jakarta.inject.Inject
import spock.lang.Specification

@MicronautTest
class LoginControllerSpec extends Specification {

    @Inject
    @Client('/')
    HttpClient client

    @Inject
    ObjectMapper mapper

    @Inject
    JsonWebTokenValidator validator

    @SuppressWarnings('GroovyAssignabilityCheck')
    def 'retorna um token coerente para o caso de login bem sucedido'() {
        given:
        String loginData = """{
                "username": "usuario$role",
                "password": "$role"
        }"""

        when:
        HttpResponse<String> response = client.toBlocking().exchange(
                HttpRequest.POST('/login', loginData), String
        )
        String loginResponse = response.body()
        Map<String, Object> mapped = mapper.readValue(loginResponse, Map)
        //noinspection GroovyUncheckedAssignmentOfMemberOfRawType
        JWT jwt = validator.validate(mapped.access_token, null).orElse(null)


        then:
        response.status == HttpStatus.OK
        mapped.token_type == 'Bearer'
        mapped.roles[0] == role.toUpperCase()
        mapped.expires_in
        mapped.access_token
        jwt.JWTClaimsSet.issuer == "jwt-demo"
        jwt.JWTClaimsSet.subject == "usuario$role"

        where:
        _ | role
        _ | 'admin'
        _ | 'updater'
        _ | 'viewer'

    }

    @SuppressWarnings('GroovyAssignabilityCheck')
    def 'retorna um token coerente para o caso de login de servico bem sucedido'() {
        given:
        String loginData = """{
            "clientId": "updater-app", 
            "secret": "meu-client-secret-que-precisa-ser-grande"
        }"""

        when:
        HttpResponse<String> response = client.toBlocking().exchange(
                HttpRequest.POST('/token', loginData), String
        )
        String loginResponse = response.body()
        Map<String, Object> mapped = mapper.readValue(loginResponse, Map)
        //noinspection GroovyUncheckedAssignmentOfMemberOfRawType
        JWT jwt = validator.validate(mapped.access_token, null).orElse(null)


        then:
        response.status == HttpStatus.OK
        mapped.token_type == 'Bearer'
        mapped.roles[0] == 'UPDATER'
        mapped.expires_in
        mapped.access_token
        jwt.JWTClaimsSet.issuer == "jwt-demo"
        jwt.JWTClaimsSet.subject == "updater-app"
        jwt.JWTClaimsSet.getClaim('isService')

    }

}
