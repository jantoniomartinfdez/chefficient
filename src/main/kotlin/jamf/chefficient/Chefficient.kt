package jamf.chefficient

import io.javalin.Javalin
import io.javalin.apibuilder.ApiBuilder.*
import io.javalin.openapi.OpenApiInfo
import io.javalin.openapi.plugin.OpenApiPlugin
import io.javalin.openapi.plugin.swagger.SwaggerPlugin
import jamf.chefficient.infrastructure.configuration.BootstrappingService
import jamf.chefficient.infrastructure.configuration.ProductionBootstrappingService
import jamf.chefficient.infrastructure.configuration.ServiceLocator
import jamf.chefficient.infrastructure.http.controller.RecipeController
import jamf.chefficient.infrastructure.http.security.AuthenticationHandler

class Chefficient(private val bootstrappingService: BootstrappingService) {
    init {
        bootstrappingService.startUp()
    }

    private val recipeController = ServiceLocator.getService(
        RecipeController::class.qualifiedName!!
    ) as RecipeController

    private val authenticationHandler = ServiceLocator.getService(
        AuthenticationHandler::class.qualifiedName!!
    ) as AuthenticationHandler

    val app: Javalin = Javalin.create { config ->
        config.registerPlugin(
            OpenApiPlugin {
                it.documentationPath = "/openapi"
                it.withDefinitionConfiguration { _, definitionConfiguration ->
                    definitionConfiguration.withInfo { info: OpenApiInfo ->
                        info.title = "Chefficient"
                        info.summary = "Optimize food, buy smart"
                        info.description = "Chefficient is designed with the intent of smartly matching recipes to food and vice-versa, with the goal of reducing food waste and save money."
                        info.withContact { contact ->
                            contact.name = "José Antonio Martín Fernández"
                            contact.email = "j.antonio.martin.fdez@gmail.com"
                            contact.url = "https://www.linkedin.com/in/jantoniomartinfdez/"
                        }
                        info.version = "1.0.0"
                    }
                    definitionConfiguration.withSecurity { securityComponentConfiguration ->
                        securityComponentConfiguration.withBasicAuth()
                    }
                }
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
            it.beforeMatched(authenticationHandler.handle)
        }
    }

    fun run() {
        app.start(bootstrappingService.javalinPort())
    }
}

fun main() {
    Chefficient(ProductionBootstrappingService).run()
}