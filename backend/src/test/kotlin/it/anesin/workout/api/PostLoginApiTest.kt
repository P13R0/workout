package it.anesin.workout.api

import com.typesafe.config.Config
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.restassured.RestAssured
import io.restassured.http.ContentType
import io.vertx.core.Vertx
import io.vertx.core.json.JsonObject
import io.vertx.ext.web.Router
import io.vertx.junit5.VertxExtension
import io.vertx.junit5.VertxTestContext
import org.hamcrest.CoreMatchers
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(VertxExtension::class)
internal class PostLoginApiTest {
  private val port = 9999
  private val adminConfig = mockk<Config>()

  @BeforeEach
  internal fun setUp(vertx: Vertx, test: VertxTestContext) {
    val router = Router.router(vertx).errorHandler(500) { it.failure().printStackTrace() }
    vertx.createHttpServer().requestHandler(router).listen(port, test.succeedingThenComplete())

    every { adminConfig.getString("username") } returns "aUsername"
    every { adminConfig.getString("password") } returns "aPassword"

    PostLoginApi(router, adminConfig)
  }

  @Test
  internal fun `should login the admin and return a jwt`() {
    RestAssured.given()
      .port(port)
      .contentType(ContentType.JSON)
      .body(JsonObject().put("username", "aUsername").put("password", "aPassword").toString())
      .post("/api/login")
      .then()
      .statusCode(200)
      .body("token", CoreMatchers.`is`("aToken"))

    verify { adminConfig.getString("username") }
    verify { adminConfig.getString("password") }
  }

  @Test
  internal fun `should return an auth error if username or password are wrong`() {
    RestAssured.given()
      .port(port)
      .contentType(ContentType.JSON)
      .body(JsonObject().put("username", "wrongUsername").put("password", "wrongPassword").toString())
      .post("/api/login")
      .then()
      .statusCode(403)
  }
}
