package jamf.chefficient.infrastructure.http.security

import io.javalin.http.Handler
import io.javalin.http.UnauthorizedResponse
import io.javalin.security.BasicAuthCredentials
import jamf.chefficient.infrastructure.configuration.PropertiesReader

class AuthenticationHandler(propertiesReader: PropertiesReader) {
    val handle: Handler = Handler { ctx ->
        if (ctx.basicAuthCredentials() == null) {
            throw UnauthorizedResponse()
        }

        val basicAuthCredentials = ctx.basicAuthCredentials()
        val username = propertiesReader.getValue("authentication.username")!!
        val password = propertiesReader.getValue("authentication.password")!!
        if (basicAuthCredentials!! != BasicAuthCredentials(username, password)) {
            throw UnauthorizedResponse()
        }
    }
}