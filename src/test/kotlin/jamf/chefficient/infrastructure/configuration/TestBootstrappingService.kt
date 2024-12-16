package jamf.chefficient.infrastructure.configuration

import jamf.chefficient.infrastructure.persistence.DbConnectionProviderFactory
import jamf.chefficient.infrastructure.persistence.postgresql.DatabaseService

object TestBootstrappingService : BaseBootstrappingService(), BootstrappingService {
    private val dbConnectionProvider = DbConnectionProviderFactory().fromConfiguration()

    override fun javalinPort(): Int {
        return 7071
    }

    override fun setUpDependencyInjection() {
        super.setUpDependencyInjection()

        val databaseService = DatabaseService(dbConnectionProvider)
        ServiceLocator.registerService(DatabaseService::class.qualifiedName!!, databaseService)
    }
}