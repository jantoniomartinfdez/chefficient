package jamf.chefficient.domain.recipe

import jamf.chefficient.domain.shared.Quantity

class Ingredient private constructor(private val name: String, private val quantity: Quantity) {
    fun description() = String.format("%s: %s", name.replaceFirstChar(Char::titlecase), quantity.description())

    companion object {
        private const val DELIMITER = " "

        fun create(name: String, quantity: String): Ingredient {
            guardAgainstInvalidName(name, name)

            return Ingredient(name, Quantity.create(quantity))
        }

        private fun guardAgainstInvalidName(nameWithinRecursion: String, fullName: String) {
            if (nameWithinRecursion.isBlank()) {
                throw InvalidIngredientName("Ingredient name '$fullName' should only be alphabetic!")
            }

            if (isCompoundNoun(nameWithinRecursion)) {
                nameWithinRecursion.split(DELIMITER).forEach { guardAgainstInvalidName(it, fullName) }
            } else if (nameWithinRecursion.any { !it.isLetter() }) {
                throw InvalidIngredientName("Ingredient name '$fullName' should only be alphabetic!")
            }
        }

        private fun isCompoundNoun(nameWithinRecursion: String) = nameWithinRecursion.contains(DELIMITER)
    }
}
