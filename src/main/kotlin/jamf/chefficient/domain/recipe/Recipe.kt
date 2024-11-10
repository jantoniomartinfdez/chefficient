package jamf.chefficient.domain.recipe

class Recipe private constructor(val title: String, val description: String, val recommendation: String) {
    companion object {
        fun create(title: String, ingredients: List<String>, description: String = "", recommendation: String = ""): Recipe {
            if (title.isBlank()) {
                throw MissingTitle("Recipes must have an alphanumeric title")
            }

            if (ingredients.isEmpty()) {
                throw MissingAtLeastOneIngredient("Recipes must contain at least one ingredient!")
            }

            return Recipe(title, description, recommendation)
        }
    }
}