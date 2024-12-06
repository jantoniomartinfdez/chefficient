package jamf.chefficient.infrastructure.persistence

import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import java.io.File
import java.io.FileNotFoundException


class DbConnectionProviderIntegrationTest {
    @Test
    fun `Given no DB configuration file exists, when creating the connection from it, then it should fail`() {
        val exception = assertThrows(FileNotFoundException::class.java) { DbConnectionProvider.fromConfiguration() }

        assertEquals("The file application.properties does not exist!", exception.message)
    }

    @Test
    fun `Given a DB configuration file exists, and doesn't contain DB credentials, when creating the connection from it, then it should fail`() {
        givenADbConfigurationFileExists()

        val exception = assertThrows(DbCredentialsNotFound::class.java) { DbConnectionProvider.fromConfiguration() }

        assertEquals("DB credentials within the file application.properties don't exist!", exception.message)
    }

    @Test
    @Disabled("TODO")
    fun `Given a DB configuration file exists, and contains DB credentials, when creating the connection from it, it should be correctly set up`() {
    }

    @AfterEach
    fun tearDown() {
        val workingDirectoryTheAppIsRunningFrom = System.getProperty("user.dir")
        val testResourcesDirectory = File(workingDirectoryTheAppIsRunningFrom, "target/test-classes")
        val file = File(testResourcesDirectory, "application.properties")
        file.delete()
    }


    private fun givenADbConfigurationFileExists() {
        val workingDirectoryTheAppIsRunningFrom = System.getProperty("user.dir")
        val testResourcesDirectory = File(workingDirectoryTheAppIsRunningFrom, "target/test-classes")
        val file = File(testResourcesDirectory, "application.properties")
        file.createNewFile()
    }
}