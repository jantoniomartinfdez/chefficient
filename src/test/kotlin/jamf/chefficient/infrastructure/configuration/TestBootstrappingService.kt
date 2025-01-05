package jamf.chefficient.infrastructure.configuration

import jamf.chefficient.infrastructure.persistence.DbConnectionProvider
import jamf.chefficient.infrastructure.persistence.DbConnectionProviderFactory
import jamf.chefficient.infrastructure.persistence.postgresql.DatabaseService

object TestBootstrappingService : BaseBootstrappingService(), BootstrappingService {
    override fun getDbConnectionProvider(): DbConnectionProvider {
        return DbConnectionProviderFactory().fromConfiguration("src/test/resources/application.properties")
    }

    override fun javalinPort(): Int {
        return 7071
    }

    override fun setUpDependencyInjection() {
        super.setUpDependencyInjection()

        val databaseService = DatabaseService(getDbConnectionProvider())
        ServiceLocator.registerService(DatabaseService::class.qualifiedName!!, databaseService)
    }
}