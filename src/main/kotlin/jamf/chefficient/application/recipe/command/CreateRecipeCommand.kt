package jamf.chefficient.application.recipe.command

data class CreateRecipeCommand(
    val title: String,
    val ingredients: List<Pair<String, String>>,
    val steps: List<String>,
    val description: String,
    val recommendation: String
)
