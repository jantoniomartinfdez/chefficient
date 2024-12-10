package jamf.chefficient.infrastructure.http.controller

import io.javalin.http.Context
import io.javalin.http.HttpStatus
import io.mockk.mockk
import io.mockk.verify
import jamf.chefficient.application.recipe.command.CreateRecipeCommandHandler
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class RecipeControllerTest {
    private lateinit var systemUnderTest: RecipeController

    private val createRecipeCommandHandlerStub = mockk<CreateRecipeCommandHandler>()
    private val contextStub = mockk<Context>(relaxed = true)


    @BeforeEach
    fun setUp() {
        systemUnderTest = RecipeController(createRecipeCommandHandlerStub)
    }

    @Test
    fun `Given I have a malformed or empty recipe request, when I try to create it, then it should respond Bad-Request`() {
        systemUnderTest.create.handle(contextStub)

        verify { contextStub.status(eq(HttpStatus.BAD_REQUEST.code)) }
    }
}