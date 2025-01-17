package jamf.chefficient.infrastructure.configuration

import org.apache.commons.configuration2.PropertiesConfiguration
import org.apache.commons.configuration2.builder.FileBasedConfigurationBuilder
import org.apache.commons.configuration2.builder.fluent.Parameters
import org.apache.commons.configuration2.interpol.ConfigurationInterpolator
import java.io.File
import java.io.FileNotFoundException

class PropertiesReader private constructor(private val configuration: PropertiesConfiguration) {
    fun getValue(key: String): String? = configuration.getString(key) //TODO: Add test case when returning NULL

    companion object {
        private fun initializeConfiguration(file: File): PropertiesConfiguration {
            val builder = FileBasedConfigurationBuilder(PropertiesConfiguration::class.java)
            builder.configure(Parameters().fileBased())
            builder.configure(Parameters().properties().setFile(file))

            val configuration = builder.getConfiguration()
            val interpolator: ConfigurationInterpolator = configuration.interpolator
            interpolator.registerLookup("dotenv", DotenvLookup())

            return configuration
        }

        fun create(relativePath: String): PropertiesReader {
            val file = File(relativePath)
            if (!file.exists()) {
                throw FileNotFoundException("The file ${file.name} is not found!")
            }

            if (!file.isFile) {
                throw FileNotReadable("The file '$relativePath' is not a file!")
            }

            return PropertiesReader(initializeConfiguration(file))
        }
    }
}