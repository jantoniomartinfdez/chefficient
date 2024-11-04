package jamf.chefficient.domain.recipe

class Recipe private constructor(val title: String) {
    companion object {
        fun create(title: String): Recipe {
            return Recipe(title)
        }
    }
}