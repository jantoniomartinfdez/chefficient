package jamf.chefficient.infrastructure.configuration

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test
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
        val propertiesReader = PropertiesReader.create(PROPERTIES_FILE_PATH)

        assertEquals("myValue", propertiesReader.getValue("myKey"))
    }

    @Test
    fun `should show the interpolated environment value of a certain key stored in a file`() {
        val propertiesReader = PropertiesReader.create(PROPERTIES_FILE_PATH)

        assertEquals("1234", propertiesReader.getValue("myKeyForInterpolation"))
    }

    @Test
    fun `should fail if the interpolated environment value of a certain key does not exist`() {
        val exception = assertThrows(PropertyNotFound::class.java) {
            val propertiesReader = PropertiesReader.create(PROPERTIES_FILE_PATH)
            propertiesReader.getValue("myKeyForNonExistingInterpolation")
        }

        assertEquals(
            "The interpolated environment value of key 'NON_EXISTING_ENV_KEY' does not exist!",
            exception.message
        )
    }

    companion object {
        private const val PROPERTIES_FILE_PATH = "src/test/resources/properties_reader_test.properties"
    }
}