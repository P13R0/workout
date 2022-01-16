package it.anesin.workout.db

import io.vertx.core.Vertx
import io.vertx.ext.mongo.MongoClient
import io.vertx.junit5.VertxTestContext
import it.anesin.workout.provider.AuthProvider
import it.anesin.workout.provider.DefaultAuthProvider
import it.anesin.workout.provider.PropertiesConfigProvider
import it.anesin.workout.provider.UserRole
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Timeout
import org.testcontainers.junit.jupiter.Testcontainers
import java.util.concurrent.TimeUnit

@Testcontainers
internal class MongoAuthorizationsTest : MongoDbTest() {
  private lateinit var mongoAuthorizations: MongoAuthorizations
  private lateinit var authProvider: AuthProvider

  @BeforeEach
  internal fun setUp(vertx: Vertx) {
    val mongoClient = MongoClient.createShared(vertx, testConfig())

    authProvider = DefaultAuthProvider(vertx, mongoClient, PropertiesConfigProvider().jwtKeys())
    mongoAuthorizations = MongoAuthorizations(mongoClient, authProvider.mongoUserUtil())
  }

  @Test
  @Timeout(5, unit = TimeUnit.SECONDS)
  internal fun `should return the list of roles of a user`(vertx: Vertx, test: VertxTestContext) {
    mongoAuthorizations.addRole("aUsername", UserRole.TRAINER)
      .compose { mongoAuthorizations.findRoles("aUsername") }
      .onSuccess {
        Assertions.assertThat(it).containsExactly(UserRole.TRAINER)
        test.completeNow()
      }
      .onFailure(test::failNow)
  }

  @Test
  @Timeout(5, unit = TimeUnit.SECONDS)
  internal fun `should return null if not find roles`(vertx: Vertx, test: VertxTestContext) {
    mongoAuthorizations.findRoles("aUsernameNotExist")
      .onSuccess {
        Assertions.assertThat(it).isEmpty()
        test.completeNow()
      }
      .onFailure(test::failNow)
  }
}

