package jamf.chefficient.infrastructure.http.security

import io.javalin.http.HttpStatus
import io.javalin.testtools.JavalinTest
import jamf.chefficient.Chefficient
import jamf.chefficient.infrastructure.configuration.TestBootstrappingService
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class AuthenticationHandlerEndToEndTest {
    private val app = Chefficient(TestBootstrappingService).app

    @Test
    fun `Given I am not authenticated, when I access the home page, then it should be shown`() =
        JavalinTest.test(app) { _, client ->
            val response = client.get("/")

            assertThat(response.code).isEqualTo(HttpStatus.OK.code)
        }
}
