package jamf.chefficient.infrastructure.persistence

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import java.io.FileNotFoundException

class DBConnectionProviderTest {
    @Test
    fun `Given no DB configuration file exists, when creating the connection from it, then it should fail`() {
        val exception = assertThrows(FileNotFoundException::class.java) { DBConnectionProvider.fromConfiguration() }

        assertEquals("The file application.properties does not exist within test scope", exception.message)
    }

    @Test
    @Disabled("TODO")
    fun `Given a DB configuration file exists, and doesn't contain DB credentials, when creating the connection from it, then it should fail`() {
    }

    @Test
    @Disabled("TODO")
    fun `Given a DB configuration file exists, and contains DB credentials, when creating the connection from it, it should be correctly set up`() {
    }
}