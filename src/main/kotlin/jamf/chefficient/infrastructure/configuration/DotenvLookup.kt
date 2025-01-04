package jamf.chefficient.infrastructure.configuration

import io.github.cdimascio.dotenv.Dotenv
import org.apache.commons.configuration2.interpol.Lookup

class DotenvLookup : Lookup {
    private val dotenv: Dotenv = Dotenv.load()

    override fun lookup(variable: String): Any {
        val arguments = variable.split("|")
        var envKey = arguments.first()
        var fallbackValue: String? = null
        if (arguments.size > 1) {
            fallbackValue = arguments.last()
        }

        if (dotenv[envKey] != null) {
            return dotenv[envKey]!!
        }

        if (fallbackValue != null) {
            return fallbackValue
        }

        throw PropertyNotFound("The interpolated environment value of key '$envKey' does not exist!")
    }
}