package jamf.chefficient.domain.recipe

import jamf.chefficient.domain.shared.Quantity
import jamf.chefficient.domain.shared.Type
import java.util.*

class Ingredient private constructor(private val name: String, private val quantity: Quantity) {
    fun description(): String {
        if (quantity.isCardinal()) {
            return String.format("%s %s", quantity.toString(), formatName())
        }

        return String.format("%s of %s", quantity.toString(), formatName())
    }

    private fun formatName(): String {
        val nameInLowerCase = name.lowercase(Locale.getDefault())
        if (quantity.isOneItem()) {
            return nameInLowerCase
        }

        return when(nameInLowerCase.last()) {
            'a', 'e', 'i', 'o', 'u' -> nameInLowerCase.plus("es")
            else -> nameInLowerCase.plus("s")
        }
    }

    companion object {
        fun fromString(quantity: String, name: String): Ingredient {
            val amount = quantity.split(" ").first().toFloat()
            val quantityType = quantity.split(" ").last()

            return Ingredient(name, Quantity(amount, Type.from(quantityType)))
        }
    }
}
