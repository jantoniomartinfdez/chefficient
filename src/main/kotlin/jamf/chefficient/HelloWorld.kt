package jamf.chefficient

import io.javalin.Javalin

class HelloWorld(dependency: String) {
    val app = Javalin.create(/*config*/)
    .get("/") { ctx -> ctx.result("Hello World") }
}