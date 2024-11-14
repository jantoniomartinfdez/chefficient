package jamf.chefficient.domain.food

import jamf.chefficient.domain.shared.Quantity
import jamf.chefficient.domain.shared.Type
import java.util.*

class Food private constructor(private val name: String, private val quantity: Quantity) {
    fun description(): String {
        return String.format("%s of %s in bulk", quantity.description(), formatName())
    }

    private fun formatName(): String {
        val nameInLowerCase = name.lowercase(Locale.getDefault())
        return when(nameInLowerCase.last()) {
            'a', 'e', 'i', 'o', 'u' -> nameInLowerCase.plus("es")
            else -> nameInLowerCase.plus("s")
        }
    }

    companion object {
        fun fromString(quantity: String, name: String): Food {
            val amount = quantity.split(" ").first().toFloat()
            val quantityType = quantity.split(" ").last()

            return Food(name, Quantity(amount, Type.create(quantityType)))
        }
    }
}