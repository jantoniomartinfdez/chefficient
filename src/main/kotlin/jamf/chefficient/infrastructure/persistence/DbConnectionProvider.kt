package jamf.chefficient.infrastructure.persistence

import java.sql.Connection
import java.sql.DriverManager

open class DbConnectionProvider(
    private val url: String,
    private val username: String,
    private val password: String
) {
    val connection: Connection
        get() {
            try {
                return DriverManager.getConnection(url, username, password)
            } catch (exception: Exception) {
                throw RuntimeException(exception)
            }
        }
}