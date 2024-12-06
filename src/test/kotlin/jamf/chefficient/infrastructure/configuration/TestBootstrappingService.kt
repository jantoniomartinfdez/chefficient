package jamf.chefficient.infrastructure.configuration

import jamf.chefficient.application.recipe.command.CreateRecipeCommandHandler
import jamf.chefficient.domain.recipe.RecipeRepository
import jamf.chefficient.infrastructure.persistence.DbConnectionProvider
import jamf.chefficient.infrastructure.persistence.postgresql.DatabaseService
import org.flywaydb.core.Flyway
import jamf.chefficient.infrastructure.persistence.postgresql.RecipeRepository as PostgresqlRecipeRepository

object TestBootstrappingService : BootstrappingService {
    private const val DB_URL = "jdbc:postgresql://localhost:9991/chefficient_test_db"
    private const val DB_USER = "test_user"
    private const val DB_PASSWORD = "5678"

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
            .dataSource(DB_URL, DB_USER, DB_PASSWORD)
            .locations("filesystem:src/main/resources/flyway/migrations")
            .load()
        flyway.migrate()
    }

    private fun setUpDependencyInjection() {
        val dbConnectionProvider = DbConnectionProvider(DB_URL, DB_USER, DB_PASSWORD)
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
    }
}