package jamf.chefficient.infrastructure.configuration

import java.io.File
import java.io.FileNotFoundException

class PropertiesReader(relativePath: String) {
    init {
        val file = File(relativePath)
        if (!file.exists()) {
            throw FileNotFoundException("The file $relativePath is not found!")
        }

        if (!file.isFile) {
            throw FileNotReadable("The file '$relativePath' is not a file!")
        }
    }
}