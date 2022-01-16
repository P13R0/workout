package it.anesin.workout.db

import io.vertx.core.Vertx
import io.vertx.ext.mongo.MongoClient
import io.vertx.junit5.VertxTestContext
import it.anesin.workout.TestFactory.trainerWith
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Timeout
import org.testcontainers.junit.jupiter.Testcontainers
import java.util.*
import java.util.concurrent.TimeUnit

@Testcontainers
internal class MongoTrainersTest : MongoDbTest() {
  private lateinit var mongoTrainers: MongoTrainers

  @BeforeEach
  internal fun setUp(vertx: Vertx) {
    mongoTrainers = MongoTrainers(MongoClient.createShared(vertx, testConfig()))
  }

  @Test
  @Timeout(5, unit = TimeUnit.SECONDS)
  internal fun `should add a trainer`(vertx: Vertx, test: VertxTestContext) {
    val trainer = trainerWith(UUID.randomUUID())

    mongoTrainers.add(trainer)
      .compose { mongoTrainers.find(trainer.username) }
      .onSuccess {
        assertThat(it).isEqualTo(trainer)
        test.completeNow()
      }
      .onFailure(test::failNow)
  }

  @Test
  @Timeout(5, unit = TimeUnit.SECONDS)
  internal fun `should return null if not find a trainer`(vertx: Vertx, test: VertxTestContext) {
    mongoTrainers.find("aUsernameNotExist")
      .onSuccess {
        assertThat(it).isEqualTo(null)
        test.completeNow()
      }
      .onFailure(test::failNow)
  }
}
