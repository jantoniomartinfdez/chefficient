package jamf.chefficient.infrastructure.persistence

import java.io.FileNotFoundException
import java.sql.Connection
import java.sql.DriverManager
import java.util.*

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
                ?: throw FileNotFoundException("The file application.properties does not exist!")

            val properties = Properties()
            properties.load(inputStream)
            val dbUrl = properties.getProperty("db.url")
            val dbUser = properties.getProperty("db.username")
            val dbPassword = properties.getProperty("db.password")

            if (dbUrl == null || dbUser == null || dbPassword == null) {
                throw DbCredentialsNotFound("DB credentials within the file application.properties don't exist!")
            }
        }
    }
}