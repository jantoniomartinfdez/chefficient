package jamf.chefficient.application.recipe.command

import jamf.chefficient.domain.recipe.Recipe
import jamf.chefficient.domain.recipe.RecipeRepository

class InMemoryRecipeRepository : RecipeRepository {
    private val recipes: MutableMap<String, Recipe> = hashMapOf()

    override fun contains(title: String): Boolean {
        return recipes.containsKey(title)
    }

    override fun save(recipe: Recipe) {
        recipes[recipe.title] = recipe
    }

    fun reset() {
        recipes.clear()
    }
}
