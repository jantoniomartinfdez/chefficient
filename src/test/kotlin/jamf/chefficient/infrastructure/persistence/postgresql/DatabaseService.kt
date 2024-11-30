package jamf.chefficient.infrastructure.persistence.postgresql

import jamf.chefficient.infrastructure.persistence.DBConnectionProvider

class DatabaseService(private val connectionProvider: DBConnectionProvider) {
    fun truncateTables() {
        connectionProvider.connection.use { connection ->
            var preparedStatement = connection.prepareStatement("TRUNCATE recipes")
            preparedStatement.execute()

            preparedStatement = connection.prepareStatement("TRUNCATE ingredients")
            preparedStatement.execute()
        }
    }
}