package jamf.chefficient.infrastructure.configuration

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test
import java.io.FileNotFoundException

class PropertiesReaderTest {
    @Test
    fun `should fail if properties file is not found`() {
        val exception = assertThrows(FileNotFoundException::class.java) { PropertiesReader("unknown_file.properties") }

        assertEquals("The file unknown_file.properties is not found!", exception.message)
    }

    @Test
    fun `should fail if properties file is not a file`() {
        val exception = assertThrows(FileNotReadable::class.java) { PropertiesReader(".") }

        assertEquals("The file '.' is not a file!", exception.message)
    }
}