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
            steps: List<String>,
            description: String = "",
            recommendation: String = ""
        ): Recipe {
            if (title.isBlank()) {
                throw MissingTitle("Recipes must have an alphanumeric title")
            }

            if (rowIngredients.isEmpty()) {
                throw MissingAtLeastOneIngredient("Recipes must contain at least one ingredient!")
            }

            if (steps.isEmpty()) {
                throw MissingAtLeastOneStep("Recipes must contain at least one step in order to be made!")
            }

            val invalidSteps = steps.filter {!it.containsAtLeastOneLetter()}.map { String.format("'%s'", it) }
            if (invalidSteps.isNotEmpty()) {
                throw InvalidSteps("The following steps are invalid: ${invalidSteps.joinToString(", ")}.")
            }

            val ingredients = rowIngredients.map { Ingredient.create(it.first, it.second) }

            return Recipe(title, ingredients, description, recommendation)
        }

        private fun String.containsAtLeastOneLetter() = any { it.isLetter() }
    }
}