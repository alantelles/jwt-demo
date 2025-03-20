package jwtdemo

import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpResponse
import io.micronaut.http.HttpStatus
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.http.client.exceptions.HttpClientResponseException
import io.micronaut.security.authentication.Authentication
import io.micronaut.security.token.jwt.generator.JwtTokenGenerator
import io.micronaut.test.extensions.spock.annotation.MicronautTest
import jakarta.inject.Inject
import spock.lang.Specification

@MicronautTest
class AppControllerSpec extends Specification {

    @Inject
    @Client('/')
    HttpClient client

    @Inject
    JwtTokenGenerator generator

    def 'retorna o status previsto de acordo com role'() {
        given:
        String token = generator.generateToken(Authentication.build('username', [role.toUpperCase()]), 3600).get()

        when:
        HttpResponse response = client.toBlocking().exchange(
                HttpRequest.GET(endpoint)
                        .header('Authorization', "Bearer $token")
        )

        then:
        response.status == HttpStatus.OK

        where:
        role      | endpoint
        'admin'   | '/admin'
        'admin'   | '/viewer'
        'admin'   | '/updater'
        'updater' | '/viewer'
        'updater' | '/updater'
        'viewer'  | '/viewer'

    }

    def 'retorna unauthorized se nao tiver header de autorizacao nas rotas esperadas'() {
        when:
        client.toBlocking().exchange(HttpRequest.GET("/$role"))

        then:
        HttpClientResponseException e = thrown(HttpClientResponseException)
        e.status == HttpStatus.UNAUTHORIZED

        where:
        _ | role
        _ | 'admin'
        _ | 'viewer'
        _ | 'updater'
    }

    def 'retorna forbidden de acordo com role/endpoint'() {
        given:
        String token = generator.generateToken(Authentication.build('username', [role.toUpperCase()]), 3600).get()

        when:
        client.toBlocking().exchange(
                HttpRequest.GET(endpoint)
                        .header('Authorization', "Bearer $token")
        )

        then:
        HttpClientResponseException e = thrown(HttpClientResponseException)
        e.status == HttpStatus.FORBIDDEN

        where:
        role      | endpoint
        'updater' | '/admin'
        'viewer'  | '/admin'
        'viewer'  | '/updater'

    }

}
