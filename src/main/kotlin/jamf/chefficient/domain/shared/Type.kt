package jamf.chefficient.domain.shared

import java.util.*

enum class Type(val measurementAbbreviationInSingular: String, val measurementAbbreviationInPlural: String) {
    UNIT("", ""),
    KILO("kg", "kgs"),
    PINCH("pinch", "pinches"),
    TEASPOON("teaspoon", "teaspoons");

    fun singularFormat(): String {
        // TODO: missing test/functionality for the below if statement
//        if (this == UNIT) {
//            return ""
//        }

        return measurementAbbreviationInSingular
    }

    fun pluralFormat(): String {
        if (this == UNIT) {
            return ""
        }

        return measurementAbbreviationInPlural
    }

    companion object {
        fun create(type: String): Type {
            return values().find {
                it.measurementAbbreviationInSingular == type.lowercase(Locale.getDefault())
                || it.measurementAbbreviationInPlural == type.lowercase( Locale.getDefault())
            } ?: UNIT
        }
    }
}
