package jamf.chefficient.infrastructure.http.security

import io.javalin.http.HttpStatus
import io.javalin.testtools.JavalinTest
import jamf.chefficient.Chefficient
import jamf.chefficient.infrastructure.configuration.TestBootstrappingService
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource

class AuthenticationHandlerEndToEndTest {
    private val app = Chefficient(TestBootstrappingService).app

    @ParameterizedTest
    @ValueSource(
        strings = [
            "/",
            "/swagger",
            "/webjars/swagger-ui/5.17.14/swagger-ui.css",
            "/webjars/swagger-ui/5.17.14/swagger-ui-bundle.js",
            "/webjars/swagger-ui/5.17.14/swagger-ui-standalone-preset.js",
            "/webjars/swagger-ui/5.17.14/favicon-32x32.png"
        ]
    )
    fun `Given I am not authenticated, when I access the home page, then it should be shown`(path: String) =
        JavalinTest.test(app) { _, client ->
            val response = client.get(path)

            assertThat(response.code).isEqualTo(HttpStatus.OK.code)
        }
}
