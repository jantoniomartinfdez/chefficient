package jamf.chefficient

import io.javalin.Javalin
import jamf.chefficient.infrastructure.configuration.BootstrappingService
import jamf.chefficient.infrastructure.configuration.ProductionBootstrappingService
import jamf.chefficient.infrastructure.http.controller.RecipeController

// TODO: Javalin end to end test under maven lifecycle
class Chefficient(private val bootstrappingService: BootstrappingService) {
    init {
        bootstrappingService.startUp()
    }

    val app: Javalin = Javalin.create()
        .get("/") { ctx -> ctx.result("Hello World") }
        .post("/recipes", RecipeController.create)

    fun run() {
        app.start(bootstrappingService.javalinPort())
    }
}

fun main() {
    Chefficient(ProductionBootstrappingService).run()
}