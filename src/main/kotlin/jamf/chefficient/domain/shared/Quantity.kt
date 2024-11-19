package jamf.chefficient.domain.shared

import java.math.BigDecimal

class Quantity private constructor(private val amount: Float, private val type: Type) {
    fun description(): String = String.format("%s %s", formatAmount(), formatType()).trim()

    fun canOperateWith(anotherQuantity: Quantity): Boolean {
        return type == anotherQuantity.type
    }

    private fun isLowerOrEqualToOneItem(): Boolean = amount <= 1.0F

    private fun formatType(): String {
        if (isLowerOrEqualToOneItem()) {
            return type.singularFormat()
        }

        return type.pluralFormat()
    }

    private fun formatAmount(): String {
        val bigDecimal = BigDecimal(amount.toString())
        val intValue = bigDecimal.toInt()
        val decimalValue = bigDecimal.subtract(BigDecimal(intValue))

        if (decimalValue.toString() == "0.0") {
            return amount.toInt().toString()
        }

        if (!ALLOWED_FRACTIONAL_UNITS.containsValue(amount.toString())) {
            return amount.toString()
        }

        return ALLOWED_FRACTIONAL_UNITS.filterValues { it == amount.toString() }.keys.first()
    }

    companion object {
        private val ALLOWED_FRACTIONAL_UNITS = mapOf("1/2" to "0.5", "1/4" to "0.25")

        fun create(quantity: String): Quantity {
            val quantityPortions = quantity.split(" ").toMutableList()
            val amount = parseAmount(quantityPortions)
            val quantityType = parseQuantityType(quantityPortions)

            return Quantity(amount, Type.create(quantityType))
        }

        private fun parseAmount(quantityPortions: MutableList<String>): Float {
            val amountInStringFormat = quantityPortions.first()
            val amount = if (ALLOWED_FRACTIONAL_UNITS.containsKey(amountInStringFormat)) {
                ALLOWED_FRACTIONAL_UNITS[amountInStringFormat]!!.toFloat()
            } else {
                amountInStringFormat.toFloatOrNull()
            }

            if (amount == null) {
                throw InvalidAmount("The given amount '$amountInStringFormat' is invalid!")
            }

            return amount
        }

        private fun parseQuantityType(quantityPortions: MutableList<String>): String {
            quantityPortions.removeFirst()

            return quantityPortions.joinToString(" ")
        }
    }
}

