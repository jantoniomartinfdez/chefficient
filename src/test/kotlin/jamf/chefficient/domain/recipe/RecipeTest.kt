package jamf.chefficient.domain.recipe

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
}