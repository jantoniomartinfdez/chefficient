package jamf.chefficient.domain.recipe

class Recipe private constructor(val title: String, val description: String) {
    companion object {
        fun create(title: String, description: String = "", recommendation: String = ""): Recipe {
            if (title.isBlank()) {
                throw MissingTitle("Recipes must have an alphanumeric title")
            }

            return Recipe(title, description)
        }
    }
}