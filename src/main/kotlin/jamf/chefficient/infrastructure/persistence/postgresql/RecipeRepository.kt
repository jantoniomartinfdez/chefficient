package jamf.chefficient.infrastructure.persistence.postgresql

import jamf.chefficient.domain.recipe.Recipe
import jamf.chefficient.domain.recipe.RecipeRepository
import jamf.chefficient.infrastructure.persistence.DBConnectionProvider

class RecipeRepository(private val connectionProvider: DBConnectionProvider) : RecipeRepository {
    override fun contains(title: String): Boolean {
        TODO("Not yet implemented")
    }

    override fun save(recipe: Recipe) {
        TODO("Not yet implemented")
    }

    fun find(title: String): Recipe? {
        TODO("Not yet implemented")
    }
}