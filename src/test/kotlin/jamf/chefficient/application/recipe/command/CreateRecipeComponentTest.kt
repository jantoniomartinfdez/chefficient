package jamf.chefficient.application.recipe.command

import jamf.chefficient.application.recipe.exception.RecipeAlreadyExists
import jamf.chefficient.domain.recipe.Recipe
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test

class CreateRecipeComponentTest {

    private var recipeRepository = InMemoryRecipeRepository()
    private val systemUnderTest = CreateRecipeCommandHandler(recipeRepository)

    @AfterEach
    fun tearDown() {
        recipeRepository.reset()
    }

    @Test
    fun `Given a recipe titled 'French Omelette' already exists, when creating another one, then throw an error`() {
        givenARecipeTitledFrenchOmeletteAlreadyExists()
        val command = whenCreatingARecipeTitledFrenchOmelette()
        thenThrowAnError(command)
    }

    private fun givenARecipeTitledFrenchOmeletteAlreadyExists() {
        recipeRepository.save(
            Recipe.create(
                "French omelette",
                listOf(
                    Pair("egg", "3"),
                    Pair("salt", "2 pinches"),
                    Pair("black pepper", "1 pinch"),
                    Pair("olive oil", "2 teaspoons")
                ),
                listOf("Sample step"),
                "Sample description",
                "Sample recommendation"
            )
        )
    }

    private fun whenCreatingARecipeTitledFrenchOmelette(): CreateRecipeCommand {
        return CreateRecipeCommand(
            "French omelette",
            listOf(
                Pair("egg", "3"),
                Pair("salt", "2 pinches"),
                Pair("black pepper", "1 pinch"),
                Pair("olive oil", "2 teaspoons")
            ),
            listOf("Sample step"),
            "Sample description",
            "Sample recommendation"
        )
    }

    private fun thenThrowAnError(command: CreateRecipeCommand) {
        val exception = assertThrows(RecipeAlreadyExists::class.java) { systemUnderTest.handle(command) }

        assertEquals("The recipe 'French omelette' already exists!", exception.message)
    }
}
