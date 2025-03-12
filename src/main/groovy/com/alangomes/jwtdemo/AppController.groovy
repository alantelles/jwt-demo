//file:noinspection GrMethodMayBeStatic
package com.alangomes.jwtdemo

import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.security.annotation.Secured

@Controller
class AppController {

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

    private Map<String, String> getResponse(String message) {
        return [message: message]
    }

}
