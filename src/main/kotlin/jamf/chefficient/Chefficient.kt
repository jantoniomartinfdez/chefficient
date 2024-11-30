package jamf.chefficient

import io.javalin.Javalin
import jamf.chefficient.infrastructure.configuration.BootstrappingService
import jamf.chefficient.infrastructure.http.controller.RecipeController

// TODO: Restore HTTP entrypoint
class Chefficient(bootstrappingService: BootstrappingService) {
    init {
        bootstrappingService.startUp()
    }

    val app: Javalin = Javalin.create()
        .get("/") { ctx -> ctx.result("Hello World") }
        .post("/recipes", RecipeController.create)
}