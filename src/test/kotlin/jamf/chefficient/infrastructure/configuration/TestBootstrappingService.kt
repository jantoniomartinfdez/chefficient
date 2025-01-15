package jamf.chefficient.infrastructure.configuration

import jamf.chefficient.infrastructure.persistence.postgresql.DatabaseService

object TestBootstrappingService : BaseBootstrappingService(), BootstrappingService {
    override fun getPropertiesFileRelativePath() = "src/test/resources/application.properties"

    override fun javalinPort(): Int {
        return 7071
    }

    override fun setUpDependencyInjection() {
        super.setUpDependencyInjection()

        val databaseService = DatabaseService(getDbConnectionProvider())
        ServiceLocator.registerService(DatabaseService::class.qualifiedName!!, databaseService)
    }
}