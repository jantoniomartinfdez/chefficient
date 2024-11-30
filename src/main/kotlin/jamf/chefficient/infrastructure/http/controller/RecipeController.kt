package jamf.chefficient.infrastructure.http.controller

import io.javalin.http.Handler
import jamf.chefficient.application.recipe.command.CreateRecipeCommand
import jamf.chefficient.application.recipe.command.CreateRecipeCommandHandler
import jamf.chefficient.infrastructure.configuration.ServiceLocator
import jamf.chefficient.infrastructure.persistence.http.request.RecipeRequest
import kotlinx.serialization.json.Json

object RecipeController {
    val create: Handler = Handler { ctx ->
        val recipeRequest: RecipeRequest = Json.decodeFromString<RecipeRequest>(ctx.body())
        val createRecipeCommandHandler: CreateRecipeCommandHandler = getCommandHandler()
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

    private fun getCommandHandler() = ServiceLocator.getService(
        CreateRecipeCommandHandler::class.qualifiedName!!
    ) as CreateRecipeCommandHandler
}