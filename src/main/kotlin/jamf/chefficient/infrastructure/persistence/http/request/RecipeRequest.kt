package jamf.chefficient.infrastructure.persistence.http.request

import kotlinx.serialization.Serializable

@Serializable
data class RecipeRequest(
    val title: String,
    val ingredients: List<Pair<String, String>>,
    val steps: List<String>,
    val description: String,
    val recommendation: String
)
