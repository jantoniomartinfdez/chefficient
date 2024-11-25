package jamf.chefficient.domain.recipe

interface RecipeRepository {
    fun contains(recipe: Recipe): Boolean
    fun save(recipe: Recipe)
}