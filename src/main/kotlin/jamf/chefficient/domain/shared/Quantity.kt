package jamf.chefficient.domain.shared

import java.math.BigDecimal

class Quantity(private val amount: Float, private val type: Type) {
    override fun toString(): String {
        return String.format("%s %s", formatAmount(), formatType()).trim()
    }

    fun isOneItem(): Boolean = amount.toInt() == 1

    fun isCardinal(): Boolean {
        return type == Type.UNIT
    }

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
}

