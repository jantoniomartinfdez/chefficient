package jamf.chefficient.domain.shared

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import org.junit.jupiter.params.provider.ValueSource

internal class QuantityTest {
    @ParameterizedTest
    @ValueSource(strings = ["3", "1 pinch", "2 pinches", "2 teaspoons", "1/2", "1/2 teaspoon", "1/4 kg"])
    fun `should show the description for valid inputs`(quantityString: String) {
        val quantity = Quantity.create(quantityString)

        assertEquals(quantityString, quantity.description())
    }

    @ParameterizedTest
    @MethodSource("pairOfInputsAndInvalidAmountsMethodSource")
    fun `should fail when provided amount is invalid`(quantityString: String, invalidAmount: String) {
        val exception = Assertions.assertThrows(
            InvalidAmount::class.java,
        ) {
            Quantity.create(quantityString)
        }

        assertEquals("The given amount '$invalidAmount' is invalid!", exception.message)
    }


    @Test
    fun `should fail when provided measurement is not found`() {
        val exception = Assertions.assertThrows(
            MeasurementNotFound::class.java,
        ) {
            Quantity.create("1 non-existing measurement")
        }

        assertEquals("The given measurement 'non-existing measurement' doesn't exist!", exception.message)
    }

    @ParameterizedTest
    @MethodSource("pairOfOperableQuantitiesMethodSource")
    fun `should tell if a certain quantity can operate with another`(
        certainQuantityString: String, anotherQuantityString: String, canOperate: Boolean
    ) {
        val certainQuantity = Quantity.create(certainQuantityString)
        val anotherQuantity = Quantity.create(anotherQuantityString)

        assertEquals(canOperate, certainQuantity.canOperateWith(anotherQuantity))
    }

    companion object {
        @JvmStatic
        fun pairOfOperableQuantitiesMethodSource() = listOf(
            Arguments.of("1234", "5678", true),
            Arguments.of("1 pinch", "1 pinch", true),
            Arguments.of("1 pinch", "2 pinches", true),
            Arguments.of("2 teaspoons", "1 teaspoon", true),

            Arguments.of("1234", "1 pinch", false),
            Arguments.of("1 pinch", "1234", false),
            Arguments.of("1 pinch", "1 kg", false),
            Arguments.of("1 teaspoons", "1234", false),
        )

        @JvmStatic
        fun pairOfInputsAndInvalidAmountsMethodSource() = listOf(
            Arguments.of("1/1234", "1/1234"),
            Arguments.of("1/3 teaspoon", "1/3"),
            Arguments.of("1/8 kg", "1/8"),
            Arguments.of("Half kg", "Half")
        )
    }
}