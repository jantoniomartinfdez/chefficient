package jamf.chefficient.infrastructure.persistence

import jamf.chefficient.infrastructure.configuration.PropertiesReader
import java.io.File

class DbConnectionProviderFactory {
    fun fromConfiguration(relativePath: String): DbConnectionProvider {
        val propertiesReader = PropertiesReader.create(relativePath)

        return createInstance(propertiesReader, relativePath)
    }

    private fun createInstance(propertiesReader: PropertiesReader, relativePath: String): DbConnectionProvider {
        val dbUrl = propertiesReader.getValue("db.url")
        val dbUser = propertiesReader.getValue("db.username")
        val dbPassword = propertiesReader.getValue("db.password")

        if (dbUrl == null || dbUser == null || dbPassword == null) {
            val file = File(relativePath)

            throw DbCredentialsNotFound("DB credentials within the file ${file.name} don't exist!")
        }

        return DbConnectionProvider(dbUrl, dbUser, dbPassword)
    }
}