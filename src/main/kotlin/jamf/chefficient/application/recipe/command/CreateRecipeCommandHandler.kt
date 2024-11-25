package jamf.chefficient.application.recipe.command

import jamf.chefficient.application.recipe.exception.RecipeAlreadyExists
import jamf.chefficient.domain.recipe.RecipeRepository

class CreateRecipeCommandHandler(private val recipeRepository: RecipeRepository) {
    fun handle(command: CreateRecipeCommand) {
        if (recipeRepository.contains(command.title)) {
            throw RecipeAlreadyExists("The recipe '${command.title}' already exists!")
        }
    }
}