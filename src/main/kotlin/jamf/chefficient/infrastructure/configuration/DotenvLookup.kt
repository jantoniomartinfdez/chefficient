package jamf.chefficient.infrastructure.configuration

import io.github.cdimascio.dotenv.Dotenv
import org.apache.commons.configuration2.interpol.Lookup

class DotenvLookup : Lookup {
    private val dotenv: Dotenv = Dotenv.load()

    override fun lookup(variable: String): Any {
        if (dotenv[variable] == null) {
            throw PropertyNotFound("The interpolated environment value of key '$variable' does not exist!")
        }

        return dotenv[variable]!!
    }
}