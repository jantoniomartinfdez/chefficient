package jamf.chefficient.infrastructure.http.controller

import io.javalin.http.Context
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import jamf.chefficient.application.recipe.command.CreateRecipeCommandHandler
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class RecipeControllerTest {
    private var systemUnderTest: RecipeController? = null

    private val createRecipeCommandHandlerStub = mockk<CreateRecipeCommandHandler>()
    private lateinit var contextStub: Context


    @BeforeEach
    fun setUp() {
        MockKAnnotations.init(this)
        systemUnderTest = RecipeController(createRecipeCommandHandlerStub)
        contextStub = mockk<Context>()
    }

    @Test
    fun `Given I have a malformed recipe request, when I try to create it, then it should respond Bad-Request`() {
        every { contextStub.body() } returns "{}"
        every { contextStub.status(any<Int>()) } returns contextStub

        systemUnderTest!!.create.handle(contextStub)

        verify { contextStub.status(eq(400)) }
    }
}