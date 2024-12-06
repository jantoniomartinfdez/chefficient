package jamf.chefficient.infrastructure.persistence

import java.sql.Connection
import java.sql.DriverManager

open class DbConnectionProvider(val url: String, val username: String, val password: String) {
    val connection: Connection
        get() {
            try {
                return DriverManager.getConnection(url, username, password)
            } catch (exception: Exception) {
                throw RuntimeException(exception)
            }
        }
}