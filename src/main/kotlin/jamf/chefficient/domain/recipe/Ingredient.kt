package jamf.chefficient.domain.recipe

import jamf.chefficient.domain.shared.Quantity

class Ingredient private constructor(private val name: String, private val quantity: Quantity) {
    fun description() = String.format("%s: %s", name.replaceFirstChar(Char::titlecase), quantity.description())

    companion object {
        /**
         * TODO: check invalid name
         */
        fun create(name: String, quantity: String): Ingredient {
            return Ingredient(name, Quantity.create(quantity))
        }
    }
}
