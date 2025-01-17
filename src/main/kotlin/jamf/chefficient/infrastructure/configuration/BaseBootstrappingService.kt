package jamf.chefficient.infrastructure.configuration

import jamf.chefficient.application.recipe.command.CreateRecipeCommandHandler
import jamf.chefficient.infrastructure.http.controller.RecipeController
import jamf.chefficient.infrastructure.http.security.AuthenticationHandler
import jamf.chefficient.infrastructure.persistence.DbConnectionProvider
import jamf.chefficient.infrastructure.persistence.DbConnectionProviderFactory
import jamf.chefficient.infrastructure.persistence.postgresql.RecipeRepository
import org.flywaydb.core.Flyway

abstract class BaseBootstrappingService : BootstrappingService {
    private var dbConnectionProvider: DbConnectionProvider? = null

    override fun startUp() {
        runDbMigrations()
        setUpDependencyInjection()
    }

    private fun runDbMigrations() {
        // Run Flyway to migrate the database schema
        val flyway = Flyway.configure()
            .dataSource(
                getDbConnectionProvider().url,
                getDbConnectionProvider().username,
                getDbConnectionProvider().password
            )
            .locations("classpath:flyway/migrations")
            .load()
        flyway.migrate()
    }

    protected open fun setUpDependencyInjection() {
        val propertiesReader = PropertiesReader.create(getPropertiesFileRelativePath())

        ServiceLocator.registerService(DbConnectionProvider::class.qualifiedName!!, getDbConnectionProvider())

        val recipeRepository = RecipeRepository(getDbConnectionProvider())
        ServiceLocator.registerService(
            jamf.chefficient.domain.recipe.RecipeRepository::class.qualifiedName!!,
            recipeRepository
        )

        val createRecipeCommandHandler = CreateRecipeCommandHandler(recipeRepository)
        ServiceLocator.registerService(
            CreateRecipeCommandHandler::class.qualifiedName!!,
            createRecipeCommandHandler
        )

        val recipeController = RecipeController(createRecipeCommandHandler)
        ServiceLocator.registerService(RecipeController::class.qualifiedName!!, recipeController)

        val authenticationHandler = AuthenticationHandler(propertiesReader)
        ServiceLocator.registerService(AuthenticationHandler::class.qualifiedName!!, authenticationHandler)
    }

    protected fun getDbConnectionProvider(): DbConnectionProvider {
        if (dbConnectionProvider == null) {
            dbConnectionProvider = DbConnectionProviderFactory().fromConfiguration(getPropertiesFileRelativePath())
        }

        return dbConnectionProvider!!
    }

    protected abstract fun getPropertiesFileRelativePath(): String
}