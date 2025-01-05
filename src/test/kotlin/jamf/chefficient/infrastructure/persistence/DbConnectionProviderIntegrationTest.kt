package jamf.chefficient.infrastructure.persistence

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.io.FileNotFoundException


class DbConnectionProviderIntegrationTest {
    private var systemUnderTest: DbConnectionProviderFactory? = null
    private val workingDirectoryTheAppIsRunningFrom = "src/test/resources/"

    @BeforeEach
    fun setUp() {
        systemUnderTest = DbConnectionProviderFactory()
    }

    @Test
    fun `Given no DB configuration file exists, when creating the connection from it, then it should fail`() {
        val exception = assertThrows(FileNotFoundException::class.java) {
            systemUnderTest!!.fromConfiguration(getRelativePath("non_existing_file.properties"))
        }

        assertEquals("The file non_existing_file.properties is not found!", exception.message)
    }

    @Test
    fun `Given a DB configuration file exists, and doesn't contain DB credentials, when creating the connection from it, then it should fail`() {
        val exception = assertThrows(DbCredentialsNotFound::class.java) {
            systemUnderTest!!.fromConfiguration(getRelativePath("db_connection_provider_integration_test_with_no_db_credentials.properties"))
        }

        assertEquals(
            "DB credentials within the file db_connection_provider_integration_test_with_no_db_credentials.properties don't exist!",
            exception.message
        )
    }

    @Test
    fun `Given a DB configuration file exists, and contains DB credentials, when creating the connection from it, then it should be correctly set up`() {
        val dbConnectionProvider = systemUnderTest!!.fromConfiguration(getRelativePath("application.properties"))

        thenItShouldBeCorrectlySetUp(dbConnectionProvider)
    }

    private fun getRelativePath(fileName: String) = workingDirectoryTheAppIsRunningFrom + fileName

    private fun thenItShouldBeCorrectlySetUp(dbConnectionProvider: DbConnectionProvider) {
        assertInstanceOf(DbConnectionProvider::class.java, dbConnectionProvider)
        assertThat(dbConnectionProvider).usingRecursiveComparison().isEqualTo(
            DbConnectionProvider(
                "jdbc:postgresql://localhost:9991/chefficient_test_db", "test_user", "5678"
            )
        )
    }
}
