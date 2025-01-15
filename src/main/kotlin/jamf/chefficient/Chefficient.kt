package jamf.chefficient

import io.javalin.Javalin
import io.javalin.apibuilder.ApiBuilder.*
import io.javalin.http.Handler
import io.javalin.http.UnauthorizedResponse
import io.javalin.openapi.plugin.OpenApiPlugin
import io.javalin.openapi.plugin.swagger.SwaggerPlugin
import io.javalin.security.BasicAuthCredentials
import jamf.chefficient.infrastructure.configuration.BootstrappingService
import jamf.chefficient.infrastructure.configuration.ProductionBootstrappingService
import jamf.chefficient.infrastructure.configuration.ServiceLocator
import jamf.chefficient.infrastructure.http.controller.RecipeController

class Chefficient(private val bootstrappingService: BootstrappingService) {
    init {
        bootstrappingService.startUp()
    }

    private val recipeController = ServiceLocator.getService(
        RecipeController::class.qualifiedName!!
    ) as RecipeController

    private val handleAccess: Handler = Handler { ctx ->
        if (ctx.basicAuthCredentials() == null) {
            throw UnauthorizedResponse()
        }

        val basicAuthCredentials = ctx.basicAuthCredentials()
        if (!basicAuthCredentials!!.equals(BasicAuthCredentials("myUsername", "myPassword"))) {
            throw UnauthorizedResponse()
        }
    }

    val app: Javalin = Javalin.create { config ->
        config.registerPlugin(
            OpenApiPlugin {
                it.documentationPath = "/openapi"
            }
        )

        config.registerPlugin(
            SwaggerPlugin {
                it.uiPath = "/swagger"
                it.documentationPath = "/openapi"
            }
        )

        config.router.apiBuilder {
            get("/") { ctx -> ctx.redirect("/swagger") }
            path("recipes") {
                post(recipeController.create)
            }
        }

        config.router.mount {
            it.beforeMatched(handleAccess)
        }
    }

    fun run() {
        app.start(bootstrappingService.javalinPort())
    }
}

fun main() {
    Chefficient(ProductionBootstrappingService).run()
}