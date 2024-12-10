package jamf.chefficient.infrastructure.http.controller

import io.javalin.http.Handler
import jamf.chefficient.application.recipe.command.CreateRecipeCommand
import jamf.chefficient.application.recipe.command.CreateRecipeCommandHandler
import jamf.chefficient.infrastructure.persistence.http.request.RecipeRequest
import kotlinx.serialization.json.Json

class RecipeController(private val createRecipeCommandHandler: CreateRecipeCommandHandler) {
    val create: Handler = Handler { ctx ->
        var recipeRequest: RecipeRequest? = null
        try {
            recipeRequest = Json.decodeFromString<RecipeRequest>(ctx.body())
        } catch (exception: Throwable) {
            ctx.status(400)

            return@Handler
        }
        createRecipeCommandHandler.handle(
            CreateRecipeCommand(
                recipeRequest.title,
                recipeRequest.ingredients,
                recipeRequest.steps,
                recipeRequest.description,
                recipeRequest.recommendation
            )
        )

        ctx.status(201)
    }
}