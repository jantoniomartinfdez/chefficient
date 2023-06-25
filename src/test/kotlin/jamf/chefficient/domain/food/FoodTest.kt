package jamf.chefficient.domain.food

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class FoodTest {

    @Test
    fun `when having 3 kilos of tomatoes in bulk, they should describe what they are`() {
        val food = Food.fromString("3 Kg", "Tomato")

        val expected = "3 kilos of tomatoes in bulk"
        assertEquals(expected, food.description())
    }
}