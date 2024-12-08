package jamf.chefficient.infrastructure.persistence

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.io.File
import java.io.FileNotFoundException
import java.io.FileWriter


class DbConnectionProviderIntegrationTest {
    private var systemUnderTest: DbConnectionProviderFactorySpy? = null

    @BeforeEach
    fun setUp() {
        systemUnderTest = DbConnectionProviderFactorySpy()
    }

    @Test
    fun `Given no DB configuration file exists, when creating the connection from it, then it should fail`() {
        givenNoDbConfigurationFileExists()

        val exception = assertThrows(FileNotFoundException::class.java) { systemUnderTest!!.fromConfiguration() }

        assertEquals("The file application.properties does not exist!", exception.message)
    }

    @Test
    fun `Given a DB configuration file exists, and doesn't contain DB credentials, when creating the connection from it, then it should fail`() {
        givenADbConfigurationFileExists()

        val exception = assertThrows(DbCredentialsNotFound::class.java) { systemUnderTest!!.fromConfiguration() }

        assertEquals("DB credentials within the file application.properties don't exist!", exception.message)
        thenItShouldReleaseSystemResourcesAfterUsingTheDbConfigurationFile()
    }

    @Test
    fun `Given a DB configuration file exists, and contains DB credentials, when creating the connection from it, then it should be correctly set up`() {
        val file = givenADbConfigurationFileExists()
        givenContainsDbCredentials(file)

        val dbConnectionProvider = systemUnderTest!!.fromConfiguration()

        thenItShouldBeCorrectlySetUp(dbConnectionProvider)
        thenItShouldReleaseSystemResourcesAfterUsingTheDbConfigurationFile()
    }

    @AfterEach
    fun tearDown() {
        givenNoDbConfigurationFileExists()
    }

    private fun givenNoDbConfigurationFileExists() {
        val workingDirectoryTheAppIsRunningFrom = System.getProperty("user.dir")
        var testResourcesDirectory = File(workingDirectoryTheAppIsRunningFrom, "target/test-classes")
        var file = File(testResourcesDirectory, "application.properties")
        file.delete()

        // JAVA class loader fetches resources from "main" and then overrides with the ones in "test" folder when running tests.
        // Thus, both need to be removed
        testResourcesDirectory = File(workingDirectoryTheAppIsRunningFrom, "target/classes")
        file = File(testResourcesDirectory, "application.properties")
        file.delete()
    }

    private fun givenADbConfigurationFileExists(): File {
        val workingDirectoryTheAppIsRunningFrom = System.getProperty("user.dir")
        val testResourcesDirectory = File(workingDirectoryTheAppIsRunningFrom, "target/test-classes")
        val file = File(testResourcesDirectory, "application.properties")
        file.createNewFile()

        return file
    }

    private fun givenContainsDbCredentials(file: File) {
        FileWriter(file).use { writer ->
            writer.write(
                """ 
                        db.url=jdbc:postgresql://localhost:9991/chefficient_test_db
                        db.username=test_user
                        db.password=5678
                    """.trimIndent()
            )
        }
    }

    private fun thenItShouldBeCorrectlySetUp(dbConnectionProvider: DbConnectionProvider) {
        assertInstanceOf(DbConnectionProvider::class.java, dbConnectionProvider)
        assertThat(dbConnectionProvider).usingRecursiveComparison().isEqualTo(
            DbConnectionProvider(
                "jdbc:postgresql://localhost:9991/chefficient_test_db",
                "test_user",
                "5678"
            )
        )
    }

    private fun thenItShouldReleaseSystemResourcesAfterUsingTheDbConfigurationFile() {
        assertTrue(systemUnderTest!!.isFileClosed(), "System resources have not been released!")
    }
}
