package jamf.chefficient.domain.shared

import java.util.*

enum class Type(val measurementAbbreviation: String) {
    UNIT(""),
    KILO("kg"),
    PINCH("pinch"),
    TEASPOON("teaspoon");

    fun singularFormat(): String {
        return measurementAbbreviation.lowercase(Locale.getDefault())
    }

    fun pluralFormat(): String {
        if (this == UNIT) {
            return ""
        }

        val nameInLowerCase = measurementAbbreviation.lowercase(Locale.getDefault())

        return when(nameInLowerCase.last()) {
            'a', 'e', 'i', 'o', 'u' -> nameInLowerCase.plus("es")
            else -> nameInLowerCase.plus("s")
        }
    }

    companion object {
        fun from(string: String): Type = values().find { it.measurementAbbreviation == string.lowercase(Locale.getDefault()) } ?: UNIT
    }
}
