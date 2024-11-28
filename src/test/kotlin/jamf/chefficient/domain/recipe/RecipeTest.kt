package jamf.chefficient.domain.recipe

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource

/**
 * TODO: it cannot contain 2 ingredients with the same name
 */
internal class RecipeTest {
    @Test
    fun `should not allow an empty title`() {
        val exception = assertThrows(
            MissingTitle::class.java
        ) { Recipe.create("", listOf(Pair("egg", "3")), listOf("Sample instruction")) }

        assertEquals("Recipes must have an alphanumeric title", exception.message)
    }

    @Test
    fun `should not allow a blank title`() {
        val exception = assertThrows(
            MissingTitle::class.java
        ) { Recipe.create(" ", listOf(Pair("egg", "3")), listOf("Sample instruction")) }

        assertEquals("Recipes must have an alphanumeric title", exception.message)
    }

    @Test
    fun `should allow an alphanumeric title`() {
        assertEquals(
            "French omelette",
            Recipe.create("French omelette", listOf(Pair("egg", "3")), listOf("Sample instruction")).title
        )
    }

    @Test
    fun `should not allow to contain no ingredients at all`() {
        val exception = assertThrows(
            MissingAtLeastOneIngredient::class.java,
        ) {
            Recipe.create(
                "French omelette",
                listOf(),
                listOf("Sample instruction")
            )
        }

        assertEquals("Recipes must contain at least one ingredient!", exception.message)
    }

    @ParameterizedTest
    @ValueSource(strings = ["", " ", "1234", "@#", "egg1234", "1234egg", "12egg34", "@egg", "12black34 pepper", "black pepper1234"])
    fun `should not allow to contain invalid ingredient names`(invalidIngredientName: String) {
        val exception = assertThrows(
            InvalidIngredientName::class.java,
        ) {
            Recipe.create(
                "French omelette",
                listOf(
                    Pair(invalidIngredientName, "3")
                ),
                listOf("Sample instruction")
            )
        }

        assertEquals("Ingredient name '$invalidIngredientName' should only be alphabetic!", exception.message)
    }

    @Test
    fun `should be made of ingredients`() {
        val ingredients = listOf("Egg: 3", "Salt: 2 pinches", "Black pepper: 1 pinch", "Olive oil: 2 teaspoons")

        assertEquals(
            ingredients,
            Recipe.create(
                "French omelette",
                listOf(
                    Pair("egg", "3"),
                    Pair("salt", "2 pinches"),
                    Pair("black pepper", "1 pinch"),
                    Pair("olive oil", "2 teaspoons")
                ),
                listOf("Sample instruction")
            ).ingredients()
        )
    }

    @Test
    fun `should not allow to contain no steps at all`() {
        val exception = assertThrows(
            MissingAtLeastOneStep::class.java,
        ) {
            Recipe.create(
                "French omelette",
                listOf(Pair("egg", "3")),
                listOf()
            )
        }

        assertEquals("Recipes must contain at least one step in order to be made!", exception.message)
    }

    @Test
    fun `should not allow to contain invalid steps`() {
        val exception = assertThrows(
            InvalidSteps::class.java,
        ) {
            Recipe.create(
                "French omelette",
                listOf(Pair("egg", "3")),
                listOf("valid 1234", "", "5678", "#@")
            )
        }

        assertEquals("The following steps are invalid: '', '5678', '#@'.", exception.message)
    }

    @Test
    fun `should only contain steps in alphanumeric format`() {
        assertEquals(
            listOf("valid 1234"),
            Recipe.create(
                "French omelette",
                listOf(Pair("egg", "3")),
                listOf("valid 1234")
            ).steps
        )
    }


    @Test
    fun `may have a description`() {
        assertEquals(
            "The easiest recipe ever!",
            Recipe.create(
                "French omelette",
                listOf(Pair("egg", "3")),
                listOf("Sample instruction"),
                "The easiest recipe ever!"
            ).description
        )
    }

    @Test
    fun `may not have a description`() {
        assertEquals(
            "",
            Recipe.create("French omelette", listOf(Pair("egg", "3")), listOf("Sample instruction")).description
        )
    }

    @Test
    fun `may have a recommendation`() {
        assertEquals(
            "Please, use fresh eggs whenever possible",
            Recipe.create(
                "French omelette",
                listOf(Pair("egg", "3")),
                listOf("Sample instruction"),
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
                listOf(Pair("egg", "3")),
                listOf("Sample instruction"),
                "The easiest recipe ever!"
            ).recommendation
        )
    }
}