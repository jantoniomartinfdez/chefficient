package jamf.chefficient.domain.food

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

/**
 * Business rules:
 *     - It should have a non-empty name
 *     - It should have a numbered quantity
 *     - It should have an existing measurable unit
 *     - It should not have a sell-by date if it is not perishable
 *     - It should have a sell-by date if it is perishable
 * Invariants:
 *     - It should not change its name
 *     - It should not change its quantity
 *     - It should not change its unit
 *     - It should not change its batch date
 *     - It should not change its sell-by date
 * Examples:
 *     - [PERISHABLE][INDUSTRIAL FOOD] Yogurt: 4 units of yogurt, batch date 01-01-2024, sell-by date 01-02-2024
 *     - [PERISHABLE][HOMEMADE FOOD] Bread: a loaf of bread, batch date NULL, sell-by date +1 week (default one when no specified but needed)
 *     - [NON-PERISHABLE][HOMEMADE FOOD] Honey: a liter of honey, batch date NULL, sell-by date NULL
 *     - [NON-PERISHABLE][INDUSTRIAL FOOD] Salt: 1 kilo of salt in bulk, batch date 01-01-2024, sell-by date NULL
 */
internal class FoodTest {

    @Test
    fun `when having 3 kilos of tomatoes in bulk, they should describe what they are`() {
        val food = Food.fromString("3 Kg", "Tomato")

        val expected = "3 kgs of tomatoes in bulk"
        assertEquals(expected, food.description())
    }
}