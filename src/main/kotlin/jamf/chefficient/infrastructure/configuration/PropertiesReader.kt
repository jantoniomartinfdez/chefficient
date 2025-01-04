package jamf.chefficient.infrastructure.configuration

import org.apache.commons.configuration2.PropertiesConfiguration
import org.apache.commons.configuration2.builder.FileBasedConfigurationBuilder
import org.apache.commons.configuration2.builder.fluent.Parameters
import java.io.File
import java.io.FileNotFoundException

class PropertiesReader(relativePath: String) {
    private var file: File

    init {
        file = File(relativePath)
        if (!file.exists()) {
            throw FileNotFoundException("The file $relativePath is not found!")
        }

        if (!file.isFile) {
            throw FileNotReadable("The file '$relativePath' is not a file!")
        }
    }

    fun getValue(key: String): String {
        val params = Parameters()
        val builder = FileBasedConfigurationBuilder(
            PropertiesConfiguration::class.java
        ).configure(params.fileBased())
        builder.configure(Parameters().properties().setFile(file))
        val configuration = builder.getConfiguration()

        return configuration.getString(key)
    }
}