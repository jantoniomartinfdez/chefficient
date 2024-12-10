package jamf.chefficient.infrastructure.configuration

import jamf.chefficient.application.recipe.command.CreateRecipeCommandHandler
import jamf.chefficient.domain.recipe.RecipeRepository
import jamf.chefficient.infrastructure.http.controller.RecipeController
import jamf.chefficient.infrastructure.persistence.DbConnectionProvider
import jamf.chefficient.infrastructure.persistence.DbConnectionProviderFactory
import jamf.chefficient.infrastructure.persistence.postgresql.DatabaseService
import org.flywaydb.core.Flyway
import jamf.chefficient.infrastructure.persistence.postgresql.RecipeRepository as PostgresqlRecipeRepository

object TestBootstrappingService : BootstrappingService {
    private val dbConnectionProvider = DbConnectionProviderFactory().fromConfiguration()

    override fun startUp() {
        runDbMigrations()
        setUpDependencyInjection()
    }

    override fun javalinPort(): Int {
        return 7071
    }

    private fun runDbMigrations() {
        // Run Flyway to migrate the database schema
        val flyway = Flyway.configure()
            .dataSource(dbConnectionProvider.url, dbConnectionProvider.username, dbConnectionProvider.password)
            .locations("filesystem:src/main/resources/flyway/migrations")
            .load()
        flyway.migrate()
    }

    private fun setUpDependencyInjection() {
        ServiceLocator.registerService(DbConnectionProvider::class.qualifiedName!!, dbConnectionProvider)

        val recipeRepository = PostgresqlRecipeRepository(dbConnectionProvider)
        ServiceLocator.registerService(RecipeRepository::class.qualifiedName!!, recipeRepository)

        val createRecipeCommandHandler = CreateRecipeCommandHandler(recipeRepository)
        ServiceLocator.registerService(
            CreateRecipeCommandHandler::class.qualifiedName!!,
            createRecipeCommandHandler
        )

        val databaseService = DatabaseService(dbConnectionProvider)
        ServiceLocator.registerService(DatabaseService::class.qualifiedName!!, databaseService)

        val recipeController = RecipeController(createRecipeCommandHandler)
        ServiceLocator.registerService(RecipeController::class.qualifiedName!!, recipeController)
    }
}