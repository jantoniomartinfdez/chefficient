package jamf.chefficient.infrastructure.http

import io.javalin.http.Header
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
import java.util.*

class RecipeEndToEndTest {
    private val app = Chefficient(TestBootstrappingService).app

    @Test
    fun `Given I have a valid recipe request, and that recipe does not exist yet, when I perform a POST operation, then it should be successfully stored`() =
        JavalinTest.test(app) { _, client ->
            val recipeJsonRequest = givenIHaveAValidRecipeRequest()
            andThatRecipeDoesNotExistYet()
            val authentication = andIAmAuthenticated("myUsername", "myPassword")

            val response = whenIPerformAPostOperation(client, recipeJsonRequest, authentication)

            thenItShouldBeSuccessfullyStored(response)
        }

    @Test
    fun `Given I am not authenticated, when I perform a POST operation, then it should be refused by an unauthorized response`() {
        JavalinTest.test(app) { _, client ->
            val recipeJsonRequest = givenIHaveAValidRecipeRequest()

            val response = whenIPerformAPostOperation(client, recipeJsonRequest)

            thenItShouldBeRefusedByAnUnauthorizedResponse(response)
        }
    }

    @Test
    fun `Given I am not authenticated with valid credentials, when I perform a POST operation, then it should be refused by an unauthorized response`() {
        JavalinTest.test(app) { _, client ->
            val recipeJsonRequest = givenIHaveAValidRecipeRequest()
            val authentication = andIAmAuthenticated("myInvalidUsername", "myInvalidPassword")

            val response = whenIPerformAPostOperation(client, recipeJsonRequest, authentication)

            thenItShouldBeRefusedByAnUnauthorizedResponse(response)
        }
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

    private fun andThatRecipeDoesNotExistYet() {
        val databaseService = ServiceLocator.getService(DatabaseService::class.qualifiedName!!) as DatabaseService
        databaseService.truncateTables()
    }

    private fun andIAmAuthenticated(username: String, password: String) =
        "Basic " + Base64.getEncoder().encodeToString(("$username:$password").toByteArray())

    private fun whenIPerformAPostOperation(client: HttpClient, jsonRequest: String, authentication: String? = null) =
        client.post("/recipes", jsonRequest) { builder ->
            authentication?.let { builder.header(Header.AUTHORIZATION, it) }
        }

    private fun thenItShouldBeSuccessfullyStored(response: Response) =
        assertThat(response.code).isEqualTo(HttpStatus.CREATED.code)

    private fun thenItShouldBeRefusedByAnUnauthorizedResponse(response: Response) =
        assertThat(response.code).isEqualTo(HttpStatus.UNAUTHORIZED.code)
}
