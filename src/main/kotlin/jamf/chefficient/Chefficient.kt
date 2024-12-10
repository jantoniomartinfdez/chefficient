package jamf.chefficient

import io.javalin.Javalin
import jamf.chefficient.infrastructure.configuration.BootstrappingService
import jamf.chefficient.infrastructure.configuration.ProductionBootstrappingService
import jamf.chefficient.infrastructure.configuration.ServiceLocator
import jamf.chefficient.infrastructure.http.controller.RecipeController

class Chefficient(private val bootstrappingService: BootstrappingService) {
    init {
        bootstrappingService.startUp()
    }

    private val recipeController = ServiceLocator.getService(RecipeController::class.qualifiedName!!) as RecipeController

    val app: Javalin = Javalin.create()
        .get("/") { ctx -> ctx.result("Hello World") }
        .post("/recipes", recipeController.create)

    fun run() {
        app.start(bootstrappingService.javalinPort())
    }
}

fun main() {
    Chefficient(ProductionBootstrappingService).run()
}