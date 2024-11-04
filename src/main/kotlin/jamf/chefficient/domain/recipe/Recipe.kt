package jamf.chefficient.domain.recipe

class Recipe private constructor(val title: String) {
    companion object {
        fun create(title: String): Recipe {
            if (title.isEmpty()) {
                throw MissingTitle("Recipes must have an alphanumeric title")
            }

            return Recipe(title)
        }
    }
}