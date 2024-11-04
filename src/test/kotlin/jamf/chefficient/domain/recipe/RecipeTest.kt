package jamf.chefficient.domain.recipe

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test

internal class RecipeTest {
    @Test
    fun `should not allow an empty title`() {
        assertThrows(
            MissingTitle::class.java,
            { Recipe.create("") },
            "Recipes must have an alphanumeric title"
        )
    }
    @Test
    fun `should not allow a blank title`() {
        assertThrows(
            MissingTitle::class.java,
            { Recipe.create(" ") },
            "Recipes must have an alphanumeric title"
        )
    }
    @Test
    fun `should allow an alphanumeric title`() {
        assertEquals("French omelette", Recipe.create("French omelette").title)
    }
    @Test
    fun `may have a description`() {
        assertEquals(
            "The easiest recipe ever!",
            Recipe.create("French omelette", "The easiest recipe ever!").description
        )
    }
    @Test
    fun `may not have a description`() {
        assertEquals(
            "",
            Recipe.create("French omelette").description
        )
    }
}