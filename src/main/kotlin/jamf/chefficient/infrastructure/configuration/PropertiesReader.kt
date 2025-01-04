package jamf.chefficient.infrastructure.configuration

import org.apache.commons.configuration2.PropertiesConfiguration
import org.apache.commons.configuration2.builder.FileBasedConfigurationBuilder
import org.apache.commons.configuration2.builder.fluent.Parameters
import java.io.File
import java.io.FileNotFoundException

class PropertiesReader private constructor(private val configuration: PropertiesConfiguration) {
    fun getValue(key: String): String = configuration.getString(key)

    companion object {
        private fun initializeConfiguration(file: File): PropertiesConfiguration {
            val builder = FileBasedConfigurationBuilder(PropertiesConfiguration::class.java)
            builder.configure(Parameters().fileBased())
            builder.configure(Parameters().properties().setFile(file))

            return builder.getConfiguration()
        }

        fun create(relativePath: String): PropertiesReader {
            val file = File(relativePath)
            if (!file.exists()) {
                throw FileNotFoundException("The file $relativePath is not found!")
            }

            if (!file.isFile) {
                throw FileNotReadable("The file '$relativePath' is not a file!")
            }

            return PropertiesReader(initializeConfiguration(file))
        }
    }
}