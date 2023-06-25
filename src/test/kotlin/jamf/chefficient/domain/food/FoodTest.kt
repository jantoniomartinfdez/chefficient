package jamf.chefficient.domain.food

import jamf.chefficient.domain.shared.Quantity
import jamf.chefficient.domain.shared.Type
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class FoodTest {

    @Test
    fun `when having 3 kilos of tomatoes in bulk, they should describe what they are`() {
        val food = Food("Tomato", Quantity(3.0F, Type.KILO))

        val expected = "3 kilos of tomatoes in bulk"
        assertEquals(expected, food.description())
    }
}