package jamf.chefficient.domain.shared

import java.util.*

enum class Type(val internalValue: String) {
    KILO("kg");

    companion object {
        fun from(string: String): Type = values().find { it.internalValue == string.lowercase(Locale.getDefault()) }!!
    }
}
