package jamf.chefficient

import io.javalin.Javalin
import jamf.chefficient.application.recipe.command.CreateRecipeCommand
import jamf.chefficient.application.recipe.command.CreateRecipeCommandHandler
import jamf.chefficient.infrastructure.persistence.DBConnectionProvider
import jamf.chefficient.infrastructure.persistence.http.request.RecipeRequest
import jamf.chefficient.infrastructure.persistence.postgresql.RecipeRepository
import kotlinx.serialization.json.Json
import org.flywaydb.core.Flyway

class HelloWorld(dependency: String) {
    val app: Javalin = Javalin.create(/*config*/)
        .get("/") { ctx -> ctx.result("Hello World") }
        .post("/recipes") { ctx ->
            val dbUrl = "jdbc:postgresql://localhost:9999/test"
            val dbUser = "test_user"
            val dbPassword = "1234"

            // Run Flyway to migrate the database schema
            val flyway = Flyway.configure()
                .dataSource(dbUrl, dbUser, dbPassword)
                .locations("filesystem:src/main/resources/flyway/migrations")
                .load()
            flyway.migrate()

            val jsonBodyString = ctx.body()
            val recipeRequest: RecipeRequest = Json.decodeFromString<RecipeRequest>(jsonBodyString)

            val recipeRepository = RecipeRepository(
                DBConnectionProvider(
                    "jdbc:postgresql://localhost:9999/test",
                    "test_user",
                    "1234"
                )
            )
            val createRecipeCommandHandler = CreateRecipeCommandHandler(recipeRepository)
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