package jamf.chefficient.domain.shared

import java.math.BigDecimal

class Quantity(private val amount: Float, private val type: Type) {
    fun description(): String = String.format("%s %s", formatAmount(), formatType()).trim()

    fun isOneItem(): Boolean = amount.toInt() == 1

    private fun formatType(): String {
        if (isOneItem()) {
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

        return amount.toString()
    }

    companion object {
        /**
         * TODO: check invalid amount + type (when not set => UNIT, when set and not found => error)
         * TODO: check invalid type => when set and not found => error
         * TODO: check type when not set => UNIT
         */
        fun create(quantity: String): Quantity {
            val amount = quantity.split(" ").first().toFloat()
            val quantityType = quantity.split(" ").last()

            return Quantity(amount, Type.create(quantityType))
        }
    }
}

