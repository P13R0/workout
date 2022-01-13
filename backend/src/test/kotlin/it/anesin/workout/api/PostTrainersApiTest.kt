package it.anesin.workout.api

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.restassured.RestAssured
import io.vertx.core.Future.succeededFuture
import io.vertx.core.Vertx
import io.vertx.core.json.JsonObject
import io.vertx.ext.web.Router
import io.vertx.junit5.VertxExtension
import io.vertx.junit5.VertxTestContext
import it.anesin.workout.DateTimeGenerator
import it.anesin.workout.IdGenerator
import it.anesin.workout.JsonExtension
import it.anesin.workout.db.Trainers
import it.anesin.workout.domain.Trainer
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import java.time.LocalDateTime
import java.util.*

@ExtendWith(VertxExtension::class, JsonExtension::class)
internal class PostTrainersApiTest {
  private val port = 9991
  private val trainers = mockk<Trainers>()
  private val idGenerator = mockk<IdGenerator>()
  private val dateTimeGenerator = mockk<DateTimeGenerator>()

  @BeforeEach
  internal fun setUp(vertx: Vertx, test: VertxTestContext) {
    val router = Router.router(vertx).errorHandler(500) { it.failure().printStackTrace() }
    vertx.createHttpServer().requestHandler(router).listen(port, test.succeedingThenComplete())

    PostTrainersApi(router, trainers, idGenerator, dateTimeGenerator)
  }

  @Test
  internal fun `should add a trainer`() {
    every { trainers.add(any()) } returns succeededFuture()
    every { idGenerator.random() } returns UUID.fromString("849c074d-55c9-4344-9dba-193c52ac072c")
    every { dateTimeGenerator.now() } returns LocalDateTime.of(2022, 10,20,6,0)

    RestAssured.given()
      .port(port)
      .body(JsonObject().put("username", "aUsername").put("name", "aName").toString())
      .post("/api/trainers")
      .then()
      .statusCode(200)

    verify { trainers.add(
      Trainer(
        UUID.fromString("849c074d-55c9-4344-9dba-193c52ac072c"),
        "aUsername",
        "aName",
        emptySet(),
        LocalDateTime.of(2022, 10,20,6,0)
      )) }
  }
}
