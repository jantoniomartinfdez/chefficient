package jamf.chefficient.domain.recipe

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow

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
}