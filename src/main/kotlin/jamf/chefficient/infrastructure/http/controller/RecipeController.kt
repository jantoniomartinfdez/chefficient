package jamf.chefficient.infrastructure.http.controller

import io.javalin.http.Handler
import io.javalin.http.HttpStatus
import io.javalin.openapi.*
import jamf.chefficient.application.recipe.command.CreateRecipeCommand
import jamf.chefficient.application.recipe.command.CreateRecipeCommandHandler
import jamf.chefficient.infrastructure.persistence.http.request.RecipeRequest
import kotlinx.serialization.json.Json

class RecipeController(private val createRecipeCommandHandler: CreateRecipeCommandHandler) {
    @OpenApi(
        summary = "Store a recipe",
        operationId = "createRecipe",
        tags = ["Recipe"],
        requestBody = OpenApiRequestBody([OpenApiContent(RecipeRequest::class)]),
        responses = [
            OpenApiResponse("201"),
            OpenApiResponse("400")
        ],
        path = PATH,
        methods = [HttpMethod.POST]
    )
    val create: Handler = Handler { ctx ->
        try {
            val recipeRequest = Json.decodeFromString<RecipeRequest>(ctx.body())
            createRecipeCommandHandler.handle(
                CreateRecipeCommand(
                    recipeRequest.title,
                    recipeRequest.ingredients,
                    recipeRequest.steps,
                    recipeRequest.description,
                    recipeRequest.recommendation
                )
            )
        } catch (throwable: Throwable) {
            // TODO: Improve error handling
            ctx.status(HttpStatus.BAD_REQUEST.code)

            return@Handler
        }

        ctx.status(HttpStatus.CREATED.code)
    }

    companion object {
        const val PATH = "/recipes"
    }
}