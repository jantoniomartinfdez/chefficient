package jamf.chefficient.domain.shared

import java.math.BigDecimal
import java.util.*

class Quantity(private val amount: Float, private val type: Type) {
    override fun toString(): String {
        return String.format("%s %s", formatAmount(), formatType())
    }

    private fun formatType(): String {
        val typeInStringFormat = type.toString().lowercase(Locale.getDefault())
        if (amount.toInt() == 1) {
            return typeInStringFormat
        }

        return typeInStringFormat + "s"
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

