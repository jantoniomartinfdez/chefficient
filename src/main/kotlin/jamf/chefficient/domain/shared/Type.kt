package jamf.chefficient.domain.shared

import java.util.*

enum class Type(val measurementAbbreviationInSingular: String, val measurementAbbreviationInPlural: String) {
    UNIT("", ""),
    KILO("kg", "kgs"),
    PINCH("pinch", "pinches"),
    TEASPOON("teaspoon", "teaspoons");

    fun singularFormat(): String = measurementAbbreviationInSingular

    fun pluralFormat(): String = measurementAbbreviationInPlural

    companion object {
        fun create(type: String): Type {
            return values().find {
                it.measurementAbbreviationInSingular == type.lowercase(Locale.getDefault())
                || it.measurementAbbreviationInPlural == type.lowercase( Locale.getDefault())
            } ?: throw MeasurementNotFound("The given measurement '$type' doesn't exist!")
        }
    }
}
