package jamf.chefficient.domain.recipe

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test

internal class RecipeTest {
    @Test
    fun `should not allow an empty title`() {
        val exception = assertThrows(
            MissingTitle::class.java
        ) { Recipe.create("", listOf("1 egg")) }

        assertEquals("Recipes must have an alphanumeric title", exception.message)
    }

    @Test
    fun `should not allow a blank title`() {
        val exception = assertThrows(
            MissingTitle::class.java
        ) { Recipe.create(" ", listOf("1 egg")) }

        assertEquals("Recipes must have an alphanumeric title", exception.message)
    }

    @Test
    fun `should allow an alphanumeric title`() {
        assertEquals("French omelette", Recipe.create("French omelette", listOf("1 egg")).title)
    }

    @Test
    fun `should not allow to contain no ingredients at all`() {
        val exception = assertThrows(
            MissingAtLeastOneIngredient::class.java,
        ) {
            Recipe.create(
                "French omelette",
                listOf()
            )
        }

        assertEquals("Recipes must contain at least one ingredient!", exception.message)
    }

    @Test
    fun `should be made of ingredients`() {
        assertEquals(
            listOf("2 eggs", "1 pinch of salt", "1 teaspoon of olive oil"),
            Recipe.create(
                "French omelette",
                listOf("2 eggs", "1 pinch of salt", "1 teaspoon of olive oil")
            ).ingredients
        )
    }

    @Test
    fun `may have a description`() {
        assertEquals(
            "The easiest recipe ever!",
            Recipe.create("French omelette", listOf("1 egg"), "The easiest recipe ever!").description
        )
    }

    @Test
    fun `may not have a description`() {
        assertEquals(
            "",
            Recipe.create("French omelette", listOf("1 egg")).description
        )
    }

    @Test
    fun `may have a recommendation`() {
        assertEquals(
            "Please, use fresh eggs whenever possible",
            Recipe.create(
                "French omelette",
                listOf("1 egg"),
                "The easiest recipe ever!",
                "Please, use fresh eggs whenever possible"
            ).recommendation
        )
    }

    @Test
    fun `may not have a recommendation`() {
        assertEquals(
            "",
            Recipe.create(
                "French omelette",
                listOf("1 egg"),
                "The easiest recipe ever!"
            ).recommendation
        )
    }
}