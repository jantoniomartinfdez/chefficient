package jamf.chefficient.infrastructure.persistence.postgresql

import jamf.chefficient.domain.recipe.Recipe
import jamf.chefficient.infrastructure.persistence.DBConnectionProvider
import org.junit.jupiter.api.*
import org.testcontainers.containers.PostgreSQLContainer
import java.sql.SQLException

internal class RecipeRepositoryIntegrationTest {
    private var recipeRepository: RecipeRepository? = null

    @BeforeEach
    fun setUp() {
        val connectionProvider = DBConnectionProvider(
            postgres.jdbcUrl,
            postgres.username,
            postgres.password
        )

        createTableDefinitions(connectionProvider)

        recipeRepository = RecipeRepository(connectionProvider)
    }

    @Test
    fun `Given a recipe is stored, when retrieving it again, then it should preserve the same information`() {
        val expectedRecipe = Recipe.create(
            "French omelette",
            listOf(
                Pair("egg", "3"),
                Pair("salt", "2 pinches"),
                Pair("black pepper", "1 pinch"),
                Pair("olive oil", "2 teaspoons")
            ),
            listOf("Step 1", "Step 2"),
            "The easiest recipe ever!",
            "Please, use fresh eggs whenever possible"
        )
        recipeRepository!!.save(expectedRecipe)

        Assertions.assertTrue(recipeRepository!!.contains("French omelette"))
        val actualRecipe = recipeRepository!!.find("French omelette")
        Assertions.assertEquals(expectedRecipe, actualRecipe)
    }

    private fun createTableDefinitions(connectionProvider: DBConnectionProvider) {
        try {
            connectionProvider.connection.use { conn ->
                val preparedStatement = conn.prepareStatement(
                    """
                        CREATE TABLE IF NOT EXISTS recipes (
                            id BIGINT NOT NULL,
                            title VARCHAR(32) NOT NULL,
                            ingredients TEXT NOT NULL,
                            steps TEXT NOT NULL,
                            description TEXT NOT NULL,
                            recommendation TEXT NOT NULL,

                            PRIMARY KEY (id),
                            UNIQUE (title)
                        )
                    """.trimIndent()
                )
                preparedStatement.execute()
            }
        } catch (sqlException: SQLException) {
            throw RuntimeException(sqlException)
        }
    }

    companion object {
        var postgres = PostgreSQLContainer(
            "postgres:16-alpine"
        )

        @JvmStatic
        @BeforeAll
        fun beforeAll() {
            postgres.start()
        }

        @JvmStatic
        @AfterAll
        fun afterAll() {
            postgres.stop()
        }
    }
}