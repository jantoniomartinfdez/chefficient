package jamf.chefficient.infrastructure.configuration

import org.flywaydb.core.Flyway

object ProductionBootstrappingService: BootstrappingService {
    override fun startUp() {
        // TODO: Parametrize this and move credentials out of the source code
        val dbUrl = "jdbc:postgresql://localhost:9990/chefficient_dev_db"
        val dbUser = "dev_user"
        val dbPassword = "1234"

        // Run Flyway to migrate the database schema
        val flyway = Flyway.configure()
            .dataSource(dbUrl, dbUser, dbPassword)
            .locations("filesystem:src/main/resources/flyway/migrations")
            .load()
        flyway.migrate()
    }

    override fun javalinPort(): Int {
        return 7070
    }
}