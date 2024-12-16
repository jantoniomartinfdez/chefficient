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
        givenDoesNotContainDbCredentials()

        val exception = assertThrows(DbCredentialsNotFound::class.java) { systemUnderTest!!.fromConfiguration() }

        assertEquals("DB credentials within the file application.properties don't exist!", exception.message)
        thenItShouldReleaseSystemResourcesAfterUsingTheDbConfigurationFile()
    }

    @Test
    fun `Given a DB configuration file exists, and contains DB credentials, when creating the connection from it, then it should be correctly set up`() {
        val dbConnectionProvider = systemUnderTest!!.fromConfiguration()

        thenItShouldBeCorrectlySetUp(dbConnectionProvider)
        thenItShouldReleaseSystemResourcesAfterUsingTheDbConfigurationFile()
    }

    @AfterEach
    fun tearDown() {
        givenADbConfigurationFileExists()
        givenContainsDbCredentials()
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

    private fun givenADbConfigurationFileExists() {
        val workingDirectoryTheAppIsRunningFrom = System.getProperty("user.dir")
        var testResourcesDirectory = File(workingDirectoryTheAppIsRunningFrom, "target/classes")
        var file = File(testResourcesDirectory, "application.properties")
        file.createNewFile()

        testResourcesDirectory = File(workingDirectoryTheAppIsRunningFrom, "target/test-classes")
        file = File(testResourcesDirectory, "application.properties")
        file.createNewFile()
    }

    private fun givenContainsDbCredentials() {
        val workingDirectoryTheAppIsRunningFrom = System.getProperty("user.dir")
        var testResourcesDirectory = File(workingDirectoryTheAppIsRunningFrom, "target/classes")
        var file = File(testResourcesDirectory, "application.properties")
        FileWriter(file).use { writer ->
            writer.write(
                """ 
                        db.url=jdbc:postgresql://localhost:9990/chefficient_dev_db
                        db.username=dev_user
                        db.password=1234
                    """.trimIndent()
            )
        }

        testResourcesDirectory = File(workingDirectoryTheAppIsRunningFrom, "target/test-classes")
        file = File(testResourcesDirectory, "application.properties")
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

    private fun givenDoesNotContainDbCredentials() {
        val workingDirectoryTheAppIsRunningFrom = System.getProperty("user.dir")
        var testResourcesDirectory = File(workingDirectoryTheAppIsRunningFrom, "target/classes")
        var file = File(testResourcesDirectory, "application.properties")
        FileWriter(file).use { writer ->
            writer.write("")
        }

        testResourcesDirectory = File(workingDirectoryTheAppIsRunningFrom, "target/test-classes")
        file = File(testResourcesDirectory, "application.properties")
        FileWriter(file).use { writer ->
            writer.write("")
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
