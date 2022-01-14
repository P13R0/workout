package it.anesin.workout

import io.vertx.core.AbstractVerticle
import io.vertx.core.Promise
import io.vertx.core.Vertx
import io.vertx.core.http.impl.HttpClientConnection.log
import io.vertx.core.json.JsonArray
import io.vertx.core.json.JsonObject
import io.vertx.ext.auth.authorization.RoleBasedAuthorization
import io.vertx.ext.mongo.MongoClient
import io.vertx.ext.web.Router
import io.vertx.ext.web.handler.AuthorizationHandler
import io.vertx.ext.web.handler.JWTAuthHandler
import it.anesin.workout.provider.UserRole.*
import it.anesin.workout.api.PostLoginApi
import it.anesin.workout.api.PostTrainersApi
import it.anesin.workout.db.MongoTrainers
import it.anesin.workout.provider.*
import java.io.FileNotFoundException
import java.util.*

class MainVerticle : AbstractVerticle() {

  override fun start(startPromise: Promise<Void>) {
    JsonMapper.setPreferences()

    val configProvider = PropertiesConfigProvider()
    val idProvider = UUIDIdProvider()
    val dateTimeProvider = UTCDateTimeProvider()
    val passwordProvider = DefaultPasswordProvider()
    val mongoClient = MongoClient.createShared(vertx, configProvider.mongo())
    val authProvider = DefaultAuthProvider(vertx, mongoClient, configProvider.jwtKeys())

    authProvider.addUser(configProvider.adminUsername(), configProvider.adminPassword(), ADMIN)

    val trainers = MongoTrainers(mongoClient)

    val router = Router.router(vertx)
      .errorHandler(401) { context -> log.warn("Unauthenticated call received: ${context.request().method()} ${context.request().uri()}") }
      .errorHandler(403) { context -> log.warn("Unauthorized call received: ${context.request().method()} ${context.request().uri()}") }
      .errorHandler(500) { context -> log.error("Internal Server Error", context.failure()) }

    router.route("/api/login").handler(authProvider.basicAuthenticationHandler())
    router.route("/api/*").handler(authProvider.jwtAuthenticationHandler())
    router.route("/api/trainers/*").handler(authProvider.adminAuthorizationHandler())

    PostLoginApi(router, authProvider.jwtAuthentication())
    PostTrainersApi(router, trainers, idProvider, dateTimeProvider, authProvider, passwordProvider)

    vertx
      .createHttpServer()
      .requestHandler(router)
      .listen(port())
      .onSuccess { server -> log.info("Workout backend server is listening on port ${server.actualPort()}") }
      .onFailure { log.error("Workout backend server failed to start", it) }
  }

  private fun port(): Int = System.getenv("PORT")?.let { Integer.parseInt(it) } ?: 8085
}

private fun main() {
  Vertx.vertx().deployVerticle(MainVerticle()) { result ->
    when {
      result.succeeded() -> println("Verticle deployed ${result.result()}")
      result.failed() -> println("Verticle NOT deployed ${result.cause()}")
    }
  }
}
