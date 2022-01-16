package it.anesin.workout.db

import io.vertx.core.Vertx
import io.vertx.ext.mongo.MongoClient
import io.vertx.junit5.VertxTestContext
import it.anesin.workout.TestFactory.traineeWith
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Timeout
import org.testcontainers.junit.jupiter.Testcontainers
import java.util.*
import java.util.concurrent.TimeUnit

@Testcontainers
internal class MongoTraineesTest : MongoDbTest() {
  private lateinit var mongoTrainees: MongoTrainees

  @BeforeEach
  internal fun setUp(vertx: Vertx) {
    mongoTrainees = MongoTrainees(MongoClient.createShared(vertx, testConfig()))
  }

  @Test
  @Timeout(5, unit = TimeUnit.SECONDS)
  internal fun `should add a trainee`(vertx: Vertx, test: VertxTestContext) {
    val trainee = traineeWith(UUID.randomUUID())

    mongoTrainees.add(trainee)
      .compose { mongoTrainees.find(trainee.username) }
      .onSuccess {
        assertThat(it).isEqualTo(trainee)
        test.completeNow()
      }
      .onFailure(test::failNow)
  }

  @Test
  @Timeout(5, unit = TimeUnit.SECONDS)
  internal fun `should return null if not find a trainee`(vertx: Vertx, test: VertxTestContext) {
    mongoTrainees.find("aUsernameNotExist")
      .onSuccess {
        assertThat(it).isEqualTo(null)
        test.completeNow()
      }
      .onFailure(test::failNow)
  }
}
