package jamf.chefficient.infrastructure.configuration

import jamf.chefficient.infrastructure.persistence.DbConnectionProvider
import jamf.chefficient.infrastructure.persistence.DbConnectionProviderFactory

object ProductionBootstrappingService : BaseBootstrappingService(), BootstrappingService {
    override fun getDbConnectionProvider(): DbConnectionProvider {
        return DbConnectionProviderFactory().fromConfiguration("config/application.properties")
    }

    override fun getPropertiesFileRelativePath() = "config/application.properties"

    override fun javalinPort(): Int {
        return 7070
    }
}