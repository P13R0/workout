package it.anesin.workout.db

import io.vertx.core.Vertx
import io.vertx.ext.mongo.MongoClient
import io.vertx.junit5.VertxTestContext
import it.anesin.workout.provider.AuthProvider
import it.anesin.workout.provider.DefaultAuthProvider
import it.anesin.workout.provider.PropertiesConfigProvider
import it.anesin.workout.provider.UserRole.*
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Timeout
import org.testcontainers.junit.jupiter.Testcontainers
import java.util.concurrent.TimeUnit

@Testcontainers
internal class MongoUsersTest : MongoDbTest() {
  private lateinit var mongoUsers: MongoUsers
  private lateinit var authProvider: AuthProvider

  @BeforeEach
  internal fun setUp(vertx: Vertx) {
    val mongoClient = MongoClient.createShared(vertx, testConfig())

    authProvider = DefaultAuthProvider(vertx, mongoClient, PropertiesConfigProvider().jwtKeys())
    mongoUsers = MongoUsers(mongoClient, authProvider.mongoUserUtil())
  }

  @Test
  @Timeout(5, unit = TimeUnit.SECONDS)
  internal fun `should add a new user`(vertx: Vertx, test: VertxTestContext) {
    mongoUsers.add("aUsername", "aPassword")
      .compose { mongoUsers.find("aUsername") }
      .onSuccess {
        assertThat(it).isTrue
        test.completeNow()
      }
      .onFailure(test::failNow)
  }

  @Test
  @Timeout(5, unit = TimeUnit.SECONDS)
  internal fun `should return false if not find a user`(vertx: Vertx, test: VertxTestContext) {
    mongoUsers.find("aUsernameNotExist")
      .onSuccess {
        assertThat(it).isFalse
        test.completeNow()
      }
      .onFailure(test::failNow)
  }
}
