package jamf.chefficient.infrastructure.configuration

import jamf.chefficient.infrastructure.persistence.DbConnectionProviderFactory
import org.flywaydb.core.Flyway

object ProductionBootstrappingService: BootstrappingService {
    private val dbConnectionProvider = DbConnectionProviderFactory().fromConfiguration()

    override fun startUp() {
        // Run Flyway to migrate the database schema
        val flyway = Flyway.configure()
            .dataSource(dbConnectionProvider.url, dbConnectionProvider.username, dbConnectionProvider.password)
            .locations("filesystem:src/main/resources/flyway/migrations")
            .load()
        flyway.migrate()
    }

    override fun javalinPort(): Int {
        return 7070
    }
}