package jamf.chefficient.infrastructure.persistence.postgresql

import jamf.chefficient.domain.recipe.Recipe
import jamf.chefficient.domain.recipe.RecipeRepository
import jamf.chefficient.infrastructure.persistence.DBConnectionProvider
import java.sql.SQLException

class RecipeRepository(private val connectionProvider: DBConnectionProvider) : RecipeRepository {
    override fun contains(title: String): Boolean {
        return find(title) != null
    }

    override fun save(recipe: Recipe) {
        try {
            connectionProvider.connection.use { connection ->
                val preparedStatement = connection.prepareStatement(
                    "INSERT INTO recipes(title, ingredients, steps, description, recommendation) VALUES(?,?,?,?,?)"
                )
                preparedStatement.setString(1, recipe.title)
                preparedStatement.setString(2, recipe.ingredients().joinToString(SEPARATOR) { it.lowercase() })
                preparedStatement.setString(3, recipe.steps.joinToString(SEPARATOR))
                preparedStatement.setString(4, recipe.description)
                preparedStatement.setString(5, recipe.recommendation)
                preparedStatement.execute()
            }
        } catch (sqlException: SQLException) {
            throw RuntimeException(sqlException)
        }
    }

    fun find(title: String): Recipe? {
        var recipe: Recipe? = null

        try {
            connectionProvider.connection.use { connection ->
                val preparedStatement = connection.prepareStatement(
                    "select * from recipes where title = ?"
                )
                preparedStatement.setString(1, title)
                val resultSet = preparedStatement.executeQuery()
                while (resultSet.next()) {
                    recipe = Recipe.create(
                        title = resultSet.getString("title"),
                        rowIngredients = resultSet.getString("ingredients").split(SEPARATOR).map {
                            val ingredientPortions = it.split(":")
                            Pair(ingredientPortions.first().trim(), ingredientPortions[1].trim())
                        },
                        steps = resultSet.getString("steps").split(SEPARATOR),
                        description = resultSet.getString("description"),
                        recommendation = resultSet.getString("recommendation"),
                    )
                }
            }
        } catch (sqlException: SQLException) {
            throw RuntimeException(sqlException)
        }

        return recipe
    }

    companion object {
        private const val SEPARATOR = "###"
    }
}