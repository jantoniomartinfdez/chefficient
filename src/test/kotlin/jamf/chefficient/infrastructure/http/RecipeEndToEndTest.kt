package jamf.chefficient.infrastructure.http

import io.javalin.http.HttpStatus
import io.javalin.testtools.JavalinTest
import jamf.chefficient.HelloWorld
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test


class RecipeEndToEndTest {

    private val app = HelloWorld("someDependency").app

    @Test
    fun `Given I have a valid recipe request, when I perform a POST operation, then it should be successfully stored`() =
        JavalinTest.test(app) { server, client ->
            val recipeJsonRequest = """
                {
                    "title":"French Omelette",
                    "ingredients": [
                        {"egg", "3"},
                        {"salt", "2 pinches"}
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

            assertThat(client.post("/recipes", recipeJsonRequest).code).isEqualTo(HttpStatus.CREATED.code)
        }
}