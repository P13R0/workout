package it.anesin.workout.api

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.restassured.RestAssured
import io.vertx.core.Future
import io.vertx.core.Vertx
import io.vertx.core.json.JsonObject
import io.vertx.ext.web.Router
import io.vertx.junit5.VertxExtension
import io.vertx.junit5.VertxTestContext
import it.anesin.workout.JsonExtension
import it.anesin.workout.TestFactory
import it.anesin.workout.db.Authorizations
import it.anesin.workout.db.Trainees
import it.anesin.workout.db.Users
import it.anesin.workout.domain.Trainee
import it.anesin.workout.provider.DateTimeProvider
import it.anesin.workout.provider.IdGenerator
import it.anesin.workout.provider.PasswordProvider
import it.anesin.workout.provider.UserRole
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import java.time.LocalDateTime
import java.util.*

@ExtendWith(VertxExtension::class, JsonExtension::class)
internal class PostTraineesApiTest {
  private val port = 9990
  private val trainees = mockk<Trainees>()
  private val idGenerator = mockk<IdGenerator>()
  private val dateTimeProvider = mockk<DateTimeProvider>()
  private val passwordProvider = mockk<PasswordProvider>()
  private val users = mockk<Users>()
  private val authorizations = mockk<Authorizations>()

  @BeforeEach
  internal fun setUp(vertx: Vertx, test: VertxTestContext) {
    val router = Router.router(vertx).errorHandler(500) { it.failure().printStackTrace() }
    vertx.createHttpServer().requestHandler(router).listen(port, test.succeedingThenComplete())

    PostTraineesApi(router, trainees, idGenerator, dateTimeProvider, passwordProvider, users, authorizations)
  }

  @Test
  internal fun `should add a new trainer`() {
    every { users.find(any()) } returns Future.succeededFuture(false)
    every { users.add(any(), any()) } returns Future.succeededFuture("userId")
    every { authorizations.findRoles(any()) } returns Future.succeededFuture(emptyList())
    every { authorizations.addRole(any(), any()) } returns Future.succeededFuture("roleId")
    every { trainees.find(any()) } returns Future.succeededFuture()
    every { trainees.add(any()) } returns Future.succeededFuture()
    every { idGenerator.random() } returns UUID.fromString("849c074d-55c9-4344-9dba-193c52ac072c")
    every { dateTimeProvider.now() } returns LocalDateTime.of(2022, 10,20,6,0)
    every { passwordProvider.random() } returns "aPassword"

    RestAssured.given()
      .port(port)
      .body(JsonObject().put("username", "aUsername").put("name", "aName").toString())
      .post("/api/trainees")
      .then()
      .statusCode(200)

    verify { users.add("aUsername", "aPassword") }
    verify { authorizations.addRole("aUsername", UserRole.TRAINEE) }
    verify { trainees.add(
      Trainee(
        UUID.fromString("849c074d-55c9-4344-9dba-193c52ac072c"),
        "aUsername",
        "aName",
        emptySet(),
        LocalDateTime.of(2022, 10,20,6,0)
      )) }
  }

  @Test
  internal fun `should return an error if trainer already added`() {
    every { users.find(any()) } returns Future.succeededFuture(true)
    every { users.add(any(), any()) } returns Future.succeededFuture("userId")
    every { authorizations.findRoles(any()) } returns Future.succeededFuture(listOf(UserRole.TRAINEE))
    every { authorizations.addRole(any(), any()) } returns Future.succeededFuture("roleId")
    every { trainees.find(any()) } returns Future.succeededFuture(TestFactory.traineeWith(UUID.randomUUID()))
    every { idGenerator.random() } returns UUID.fromString("849c074d-55c9-4344-9dba-193c52ac072c")
    every { dateTimeProvider.now() } returns LocalDateTime.of(2022, 10,20,6,0)
    every { passwordProvider.random() } returns "aPassword"

    RestAssured.given()
      .port(port)
      .body(JsonObject().put("username", "aUsername").put("name", "aName").toString())
      .post("/api/trainees")
      .then()
      .statusCode(409)

    verify (exactly = 0) { users.add(any(), any()) }
    verify (exactly = 0) { authorizations.addRole(any(), any()) }
    verify (exactly = 0) { trainees.add(any()) }
  }
}
