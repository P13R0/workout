package it.anesin.workout.api

import io.restassured.RestAssured
import io.vertx.core.Vertx
import io.vertx.ext.web.Router
import io.vertx.junit5.VertxExtension
import io.vertx.junit5.VertxTestContext
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(VertxExtension::class)
internal class PostLoginApiTest {
  private val port = 9999

  @BeforeEach
  internal fun setUp(vertx: Vertx, test: VertxTestContext) {
    val router = Router.router(vertx).errorHandler(500) { it.failure().printStackTrace() }
    vertx.createHttpServer().requestHandler(router).listen(port, test.succeedingThenComplete())

    PostLoginApi(router)
  }

  @Test
  internal fun `should login the admin and return a jwt`() {
    RestAssured.given()
      .port(port)
      .post("/api/login")
      .then()
      .statusCode(200)
  }
}
