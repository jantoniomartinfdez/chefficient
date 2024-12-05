package jamf.chefficient.infrastructure.persistence

import java.io.FileNotFoundException
import java.sql.Connection
import java.sql.DriverManager

class DBConnectionProvider(
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

    companion object {
        fun fromConfiguration() {
            val inputStream = this::class.java.classLoader.getResourceAsStream("application.properties")
            if (inputStream == null) {
                throw FileNotFoundException("The file application.properties does not exist within test scope")
            }
        }
    }
}