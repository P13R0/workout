package it.anesin.workout.db

import io.vertx.core.json.JsonObject
import io.vertx.junit5.VertxExtension
import it.anesin.workout.JsonExtension
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.extension.ExtendWith
import org.testcontainers.containers.GenericContainer
import org.testcontainers.containers.wait.strategy.Wait
import org.testcontainers.junit.jupiter.Container

@ExtendWith(VertxExtension::class, JsonExtension::class)
open class MongoDbTest {

  @Container
  val mongoContainer = MongoDbContainer("mongo:4.2").apply {
    withExposedPorts(27017)
    setWaitStrategy(Wait.forListeningPort())
  }

  class MongoDbContainer(dockerImageName: String) : GenericContainer<MongoDbContainer>(dockerImageName)

  @BeforeEach
  internal fun startMongo() {
    mongoContainer.start()
  }

  @AfterEach
  internal fun tearDownMongo() {
    mongoContainer.close()
  }

  fun testConfig() = JsonObject()
    .put("host", mongoContainer.containerIpAddress)
    .put("port", mongoContainer.getMappedPort(27017))!!
}
