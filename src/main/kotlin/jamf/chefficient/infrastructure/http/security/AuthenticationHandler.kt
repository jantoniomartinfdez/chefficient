package jamf.chefficient.infrastructure.http.security

import io.javalin.http.Handler
import io.javalin.http.UnauthorizedResponse
import io.javalin.security.BasicAuthCredentials
import jamf.chefficient.infrastructure.configuration.PropertiesReader
import jamf.chefficient.infrastructure.http.controller.RecipeController

class AuthenticationHandler(propertiesReader: PropertiesReader) {
    val handle: Handler = Handler { ctx ->
        if (!ctx.path().contains(RecipeController.PATH)) {
            return@Handler
        }

        if (ctx.basicAuthCredentials() == null) {
            throw UnauthorizedResponse()
        }

        val basicAuthCredentials = ctx.basicAuthCredentials()!!
        val basicAuthCredentialsFromConfiguration = buildBasicAuthCredentialsFromConfiguration(propertiesReader)
        if (basicAuthCredentials != basicAuthCredentialsFromConfiguration) {
            throw UnauthorizedResponse()
        }
    }

    private fun buildBasicAuthCredentialsFromConfiguration(propertiesReader: PropertiesReader) =
        BasicAuthCredentials(
            username = propertiesReader.getValue("authentication.username")!!,
            password = propertiesReader.getValue("authentication.password")!!
        )
}