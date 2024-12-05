package jamf.chefficient.infrastructure.http

import io.javalin.http.HttpStatus
import io.javalin.testtools.HttpClient
import io.javalin.testtools.JavalinTest
import jamf.chefficient.Chefficient
import jamf.chefficient.infrastructure.configuration.ServiceLocator
import jamf.chefficient.infrastructure.configuration.TestBootstrappingService
import jamf.chefficient.infrastructure.persistence.postgresql.DatabaseService
import okhttp3.Response
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class RecipeEndToEndTest {
    private val app = Chefficient(TestBootstrappingService).app

    @Test
    fun `Given I have a valid recipe request, and that recipe does not exist yet, when I perform a POST operation, then it should be successfully stored`() =
        JavalinTest.test(app) { _, client ->
            val recipeJsonRequest = givenIHaveAValidRecipeRequest()
            andThatRecipeDoesNotExistYet()

            val response = whenIPerformAPostOperation(client, recipeJsonRequest)

            thenItShouldBeSuccessfullyStored(response)
        }

    private fun andThatRecipeDoesNotExistYet() {
        val databaseService = ServiceLocator.getService(DatabaseService::class.qualifiedName!!) as DatabaseService
        databaseService.truncateTables()
    }

    private fun givenIHaveAValidRecipeRequest() = """
        {
            "title": "French Omelette",
            "ingredients": [
                {"first": "egg", "second": "3"},
                {"first": "salt", "second": "2 pinches"}
            ],
            "steps": [
                "Step 1",
                "Step 2",
                "Step 3"
            ]
            "description": "My description",
            "recommendation": "My recommendation"
        }
    """.trimIndent()

    private fun whenIPerformAPostOperation(client: HttpClient, recipeJsonRequest: String) =
        client.post("/recipes", recipeJsonRequest)

    private fun thenItShouldBeSuccessfullyStored(response: Response) =
        assertThat(response.code).isEqualTo(HttpStatus.CREATED.code)
}