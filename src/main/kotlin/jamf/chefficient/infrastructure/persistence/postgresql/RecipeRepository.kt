package jamf.chefficient.infrastructure.persistence.postgresql

import jamf.chefficient.domain.recipe.Recipe
import jamf.chefficient.domain.recipe.RecipeRepository
import jamf.chefficient.infrastructure.persistence.DbConnectionProvider
import java.sql.Connection
import java.sql.SQLException

class RecipeRepository(private val connectionProvider: DbConnectionProvider) : RecipeRepository {
    override fun contains(title: String) = find(title) != null

    override fun save(recipe: Recipe) {
        try {
            connectionProvider.connection.use { connection ->
                val preparedStatement = connection.prepareStatement(
                    "INSERT INTO recipes(title, steps, description, recommendation) VALUES(?,?,?,?)"
                )
                preparedStatement.setString(1, recipe.title)
                preparedStatement.setString(2, recipe.steps.joinToString(STEPS_SEPARATOR))
                preparedStatement.setString(3, recipe.description)
                preparedStatement.setString(4, recipe.recommendation)
                preparedStatement.executeUpdate()

                saveIngredients(recipe, connection)
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
                        rowIngredients = findIngredients(connection, resultSet.getInt("id")),
                        steps = resultSet.getString("steps").split(STEPS_SEPARATOR),
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

    private fun saveIngredients(recipe: Recipe, connection: Connection) {
        val lastInsertedRecipeId: Int? = getLastInsertedRecipeId(connection)

        recipe.ingredients().forEach {
            val preparedStatement = connection.prepareStatement("INSERT INTO ingredients(recipe_id, name) VALUES(?,?)")
            preparedStatement.setInt(1, lastInsertedRecipeId!!)
            preparedStatement.setString(2, it.lowercase())
            preparedStatement.execute()
        }
    }

    private fun getLastInsertedRecipeId(connection: Connection): Int? {
        val preparedStatement = connection.prepareStatement("SELECT MAX(id) FROM recipes")
        val resultSet = preparedStatement.executeQuery()
        while (resultSet.next()) {
            return resultSet.getInt(1)
        }

        return null
    }

    private fun findIngredients(connection: Connection, recipeId: Int): List<Pair<String, String>> {
        val ingredients: MutableList<Pair<String, String>> = mutableListOf()

        val preparedStatement = connection.prepareStatement("select * from ingredients where recipe_id = ?")
        preparedStatement.setInt(1, recipeId)
        val resultSet = preparedStatement.executeQuery()
        while (resultSet.next()) {
            val ingredientPortions = resultSet.getString("name").split(INGREDIENT_SEPARATOR)
            ingredients.add(Pair(ingredientPortions.first().trim(), ingredientPortions[1].trim()))
        }

        return ingredients
    }

    companion object {
        private const val STEPS_SEPARATOR = "###"
        private const val INGREDIENT_SEPARATOR = ":"
    }
}