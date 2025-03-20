//file:noinspection GrMethodMayBeStatic
package com.alangomes.jwtdemo

import com.alangomes.jwtdemo.dto.ServiceCredentialsDto
import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.Body
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.Post
import io.micronaut.security.annotation.Secured
import io.micronaut.security.rules.SecurityRule
import io.micronaut.security.token.render.BearerAccessRefreshToken
import jakarta.inject.Inject
import jakarta.validation.Valid

@Controller
class AppController {

    @Inject
    LoginService loginService

    @Get('/admin')
    @Secured(['ADMIN'])
    Map<String, String> adminAllowed() {
        return getResponse("If you see this, you're an ADMIN")
    }

    @Get('/updater')
    @Secured(['ADMIN', 'UPDATER'])
    Map<String, String> updaterAllowed() {
        return getResponse("If you see this, you're at least an UPDATER")
    }

    @Get('/viewer')
    @Secured(['ADMIN', 'UPDATER', 'VIEWER'])
    Map<String, String> viewerAllowed() {
        return getResponse("If you see this, you're at least a VIEWER")
    }

    @Post('/token')
    @Secured(SecurityRule.IS_ANONYMOUS)
    HttpResponse<BearerAccessRefreshToken> getToken(@Body @Valid ServiceCredentialsDto credentials) {
        BearerAccessRefreshToken response = loginService.authenticateService(credentials)
        if (!response) {
            return HttpResponse.unauthorized()
        }
        return HttpResponse.ok(response)
    }

    private Map<String, String> getResponse(String message) {
        return [message: message]
    }

}
