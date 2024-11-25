package jamf.chefficient.application.recipe.command

import jamf.chefficient.application.recipe.exception.RecipeAlreadyExists
import jamf.chefficient.domain.recipe.Recipe
import jamf.chefficient.domain.recipe.RecipeRepository

class CreateRecipeCommandHandler(private val recipeRepository: RecipeRepository) {
    fun handle(command: CreateRecipeCommand) {
        val recipe = Recipe.create(
            command.title,
            command.ingredients,
            command.steps,
            command.description,
            command.recommendation
        )
        if (recipeRepository.contains(recipe)) {
            throw RecipeAlreadyExists("The recipe '${command.title}' already exists!")
        }
    }
}