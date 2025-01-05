package jamf.chefficient.infrastructure.configuration

import io.github.cdimascio.dotenv.Dotenv
import org.apache.commons.configuration2.interpol.Lookup

class DotenvLookup : Lookup {
    private val dotenv: Dotenv = Dotenv.load()

    override fun lookup(variable: String): Any {
        val arguments = variable.split(ARGUMENTS_SEPARATOR)
        val envKey = arguments.first()
        val fallbackValue = getFallbackValue(arguments)

        if (dotenv[envKey] != null) {
            return dotenv[envKey]!!
        }

        return fallbackValue
            ?: throw PropertyNotFound("The interpolated environment value of key '$envKey' does not exist!")
    }

    private fun getFallbackValue(arguments: List<String>): String? {
        if (arguments.size > 1) {
            return arguments.last()
        }

        return null
    }

    companion object {
        private const val ARGUMENTS_SEPARATOR = "|"
    }
}