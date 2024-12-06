package jamf.chefficient.infrastructure.persistence

import java.io.FileNotFoundException
import java.io.InputStream
import java.util.*

open class DbConnectionProviderFactory {
    fun fromConfiguration(): DbConnectionProvider {
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

//        releaseSystemResources(inputStream)

        return DbConnectionProvider(dbUrl, dbUser, dbPassword)
    }

    protected open fun releaseSystemResources(inputStream: InputStream) {

    }
}