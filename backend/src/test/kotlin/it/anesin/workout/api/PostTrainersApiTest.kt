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
import it.anesin.workout.JsonExtension
import it.anesin.workout.TestFactory.trainerWith
import it.anesin.workout.db.Authorizations
import it.anesin.workout.db.Trainers
import it.anesin.workout.db.Users
import it.anesin.workout.domain.Trainer
import it.anesin.workout.provider.DateTimeProvider
import it.anesin.workout.provider.IdGenerator
import it.anesin.workout.provider.PasswordProvider
import it.anesin.workout.provider.UserRole.TRAINER
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
  private val dateTimeProvider = mockk<DateTimeProvider>()
  private val passwordProvider = mockk<PasswordProvider>()
  private val users = mockk<Users>()
  private val authorizations = mockk<Authorizations>()

  @BeforeEach
  internal fun setUp(vertx: Vertx, test: VertxTestContext) {
    val router = Router.router(vertx).errorHandler(500) { it.failure().printStackTrace() }
    vertx.createHttpServer().requestHandler(router).listen(port, test.succeedingThenComplete())

    PostTrainersApi(router, trainers, idGenerator, dateTimeProvider, passwordProvider, users, authorizations)
  }

  @Test
  internal fun `should add a new user as a trainer`() {
    every { users.find(any()) } returns succeededFuture(false)
    every { users.add(any(), any()) } returns succeededFuture("userId")
    every { authorizations.findRoles(any()) } returns succeededFuture(emptyList())
    every { authorizations.addRole(any(), any()) } returns succeededFuture("roleId")
    every { trainers.find(any()) } returns succeededFuture()
    every { trainers.add(any()) } returns succeededFuture()
    every { idGenerator.random() } returns UUID.fromString("849c074d-55c9-4344-9dba-193c52ac072c")
    every { dateTimeProvider.now() } returns LocalDateTime.of(2022, 10,20,6,0)
    every { passwordProvider.random() } returns "aPassword"

    RestAssured.given()
      .port(port)
      .body(JsonObject().put("username", "aUsername").put("name", "aName").toString())
      .post("/api/trainers")
      .then()
      .statusCode(200)

    verify { users.add("aUsername", "aPassword") }
    verify { authorizations.addRole("aUsername", TRAINER) }
    verify { trainers.add(
      Trainer(
        UUID.fromString("849c074d-55c9-4344-9dba-193c52ac072c"),
        "aUsername",
        "aName",
        emptySet(),
        LocalDateTime.of(2022, 10,20,6,0)
      )) }
  }

  @Test
  internal fun `should return an error if trainer already added`() {
    every { users.find(any()) } returns succeededFuture(true)
    every { users.add(any(), any()) } returns succeededFuture("userId")
    every { authorizations.findRoles(any()) } returns succeededFuture(listOf(TRAINER))
    every { authorizations.addRole(any(), any()) } returns succeededFuture("roleId")
    every { trainers.find(any()) } returns succeededFuture(trainerWith(UUID.randomUUID()))
    every { idGenerator.random() } returns UUID.fromString("849c074d-55c9-4344-9dba-193c52ac072c")
    every { dateTimeProvider.now() } returns LocalDateTime.of(2022, 10,20,6,0)
    every { passwordProvider.random() } returns "aPassword"

    RestAssured.given()
      .port(port)
      .body(JsonObject().put("username", "aUsername").put("name", "aName").toString())
      .post("/api/trainers")
      .then()
      .statusCode(409)

    verify (exactly = 0) { users.add(any(), any()) }
    verify (exactly = 0) { authorizations.addRole(any(), any()) }
    verify (exactly = 0) { trainers.add(any()) }
  }
}
