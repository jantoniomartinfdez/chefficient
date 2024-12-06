package jamf.chefficient.infrastructure.persistence.postgresql

import jamf.chefficient.domain.recipe.Recipe
import jamf.chefficient.infrastructure.persistence.DbConnectionProvider
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.*
import org.testcontainers.containers.PostgreSQLContainer
import java.sql.SQLException

internal class RecipeRepositoryIntegrationTest {
    private var recipeRepository: RecipeRepository? = null

    @BeforeEach
    fun setUp() {
        val connectionProvider = DbConnectionProvider(
            postgres.jdbcUrl,
            postgres.username,
            postgres.password
        )

        createTableDefinitions(connectionProvider)

        recipeRepository = RecipeRepository(connectionProvider)
    }

    @Test
    fun `Given a recipe is stored, when retrieving it again, then it should preserve the same information`() {
        val expectedRecipe = givenARecipeIsStored()

        val actualRecipe = whenRetrievingItAgain()

        thenItShouldPreserveTheSameInformation(actualRecipe, expectedRecipe)
    }

    private fun givenARecipeIsStored(): Recipe {
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

        return expectedRecipe
    }

    private fun whenRetrievingItAgain() = recipeRepository!!.find("French omelette")

    private fun thenItShouldPreserveTheSameInformation(
        actualRecipe: Recipe?,
        expectedRecipe: Recipe
    ) {
        Assertions.assertTrue(recipeRepository!!.contains("French omelette"))
        assertThat(actualRecipe).usingRecursiveComparison().isEqualTo(expectedRecipe)
    }

    private fun createTableDefinitions(connectionProvider: DbConnectionProvider) {
        try {
            connectionProvider.connection.use { conn ->
                val preparedStatement = conn.prepareStatement(
                    """
                        CREATE TABLE IF NOT EXISTS recipes (
                            id SERIAL PRIMARY KEY,
                            title VARCHAR(50) NOT NULL,
                            steps TEXT NOT NULL,
                            description TEXT NOT NULL,
                            recommendation TEXT NOT NULL,

                            UNIQUE (title)
                        );
                        
                        CREATE TABLE IF NOT EXISTS ingredients (
                            id SERIAL PRIMARY KEY,
                            recipe_id INTEGER,
                            name VARCHAR(50) NOT NULL,

                            UNIQUE (recipe_id, name)
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