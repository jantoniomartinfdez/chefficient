package jamf.chefficient.infrastructure.configuration

import jamf.chefficient.application.recipe.command.CreateRecipeCommandHandler
import jamf.chefficient.infrastructure.http.controller.RecipeController
import jamf.chefficient.infrastructure.persistence.DbConnectionProvider
import jamf.chefficient.infrastructure.persistence.DbConnectionProviderFactory
import jamf.chefficient.infrastructure.persistence.postgresql.RecipeRepository
import org.flywaydb.core.Flyway

abstract class BaseBootstrappingService : BootstrappingService {
    private val dbConnectionProvider = DbConnectionProviderFactory().fromConfiguration()

    override fun startUp() {
        runDbMigrations()
        setUpDependencyInjection()
    }

    private fun runDbMigrations() {
        // Run Flyway to migrate the database schema
        val flyway = Flyway.configure()
            .dataSource(dbConnectionProvider.url, dbConnectionProvider.username, dbConnectionProvider.password)
            .locations("filesystem:src/main/resources/flyway/migrations")
            .load()
        flyway.migrate()
    }

    protected open fun setUpDependencyInjection() {
        ServiceLocator.registerService(DbConnectionProvider::class.qualifiedName!!, dbConnectionProvider)

        val recipeRepository = RecipeRepository(dbConnectionProvider)
        ServiceLocator.registerService(jamf.chefficient.domain.recipe.RecipeRepository::class.qualifiedName!!, recipeRepository)

        val createRecipeCommandHandler = CreateRecipeCommandHandler(recipeRepository)
        ServiceLocator.registerService(
            CreateRecipeCommandHandler::class.qualifiedName!!,
            createRecipeCommandHandler
        )

        val recipeController = RecipeController(createRecipeCommandHandler)
        ServiceLocator.registerService(RecipeController::class.qualifiedName!!, recipeController)
    }
}