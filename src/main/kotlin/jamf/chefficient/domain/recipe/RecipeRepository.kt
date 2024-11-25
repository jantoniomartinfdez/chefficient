package jamf.chefficient.domain.recipe

interface RecipeRepository {
    fun contains(title: String): Boolean
    fun save(recipe: Recipe)
}