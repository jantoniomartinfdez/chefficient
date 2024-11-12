package jamf.chefficient.domain.recipe

class Recipe private constructor(
    val title: String,
    private val ingredients: List<Ingredient>,
    val description: String,
    val recommendation: String
) {

    fun ingredients(): List<String> {
        return ingredients.map { it.description() }
    }
    companion object {
        fun create(
            title: String,
            rowIngredients: List<Pair<String, String>>,
            description: String = "",
            recommendation: String = ""
        ): Recipe {
            if (title.isBlank()) {
                throw MissingTitle("Recipes must have an alphanumeric title")
            }

            if (rowIngredients.isEmpty()) {
                throw MissingAtLeastOneIngredient("Recipes must contain at least one ingredient!")
            }

            val ingredients = rowIngredients.map { Ingredient.fromString(it.first, it.second) }

            return Recipe(title, ingredients, description, recommendation)
        }
    }
}