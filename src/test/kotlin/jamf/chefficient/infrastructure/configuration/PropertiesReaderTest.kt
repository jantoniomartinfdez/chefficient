package jamf.chefficient.infrastructure.configuration

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test
import java.io.File
import java.io.FileNotFoundException


class PropertiesReaderTest {
    @Test
    fun `should fail if properties file is not found`() {
        val exception = assertThrows(FileNotFoundException::class.java) { PropertiesReader.create("unknown_file.properties") }

        assertEquals("The file unknown_file.properties is not found!", exception.message)
    }

    @Test
    fun `should fail if properties file is not a file`() {
        val exception = assertThrows(FileNotReadable::class.java) { PropertiesReader.create(".") }

        assertEquals("The file '.' is not a file!", exception.message)
    }

    @Test
    fun `should show the value of a certain key stored in a file`() {
        val file = File("src/test/resources/properties_reader_test.properties")
        val propertiesReader = PropertiesReader.create(file.path)

        assertEquals("myValue", propertiesReader.getValue("myKey"))
    }
}