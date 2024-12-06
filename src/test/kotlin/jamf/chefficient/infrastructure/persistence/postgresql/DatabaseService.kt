package jamf.chefficient.infrastructure.persistence.postgresql

import jamf.chefficient.infrastructure.persistence.DbConnectionProvider

class DatabaseService(private val connectionProvider: DbConnectionProvider) {
    fun truncateTables() {
        connectionProvider.connection.use { connection ->
            var preparedStatement = connection.prepareStatement("TRUNCATE recipes")
            preparedStatement.execute()

            preparedStatement = connection.prepareStatement("TRUNCATE ingredients")
            preparedStatement.execute()
        }
    }
}