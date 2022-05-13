package it.anesin.workout

import io.vertx.core.AbstractVerticle
import io.vertx.core.Promise
import io.vertx.core.Vertx
import io.vertx.core.http.HttpMethod
import io.vertx.core.http.impl.HttpClientConnection.log
import io.vertx.ext.mongo.MongoClient
import io.vertx.ext.web.Router
import io.vertx.ext.web.handler.CorsHandler
import it.anesin.workout.api.PostLoginApi
import it.anesin.workout.api.PostTraineesApi
import it.anesin.workout.api.PostTrainersApi
import it.anesin.workout.db.MongoAuthorizations
import it.anesin.workout.db.MongoTrainees
import it.anesin.workout.db.MongoTrainers
import it.anesin.workout.db.MongoUsers
import it.anesin.workout.provider.*
import it.anesin.workout.provider.UserRole.ADMIN
import it.anesin.workout.provider.UserRole.TRAINER

class MainVerticle : AbstractVerticle() {

  override fun start(startPromise: Promise<Void>) {
    JsonMapper.setPreferences()

    val configProvider = PropertiesConfigProvider()
    val idProvider = UUIDIdProvider()
    val dateTimeProvider = UTCDateTimeProvider()
    val passwordProvider = DefaultPasswordProvider()
    val mongoClient = MongoClient.createShared(vertx, configProvider.mongo())
    val authProvider = DefaultAuthProvider(vertx, mongoClient, configProvider.jwtKeys())

    val users = MongoUsers(mongoClient, authProvider.mongoUserUtil())
    val authorizations = MongoAuthorizations(mongoClient, authProvider.mongoUserUtil())
    val trainers = MongoTrainers(mongoClient)
    val trainees = MongoTrainees(mongoClient)

    setAdminUser(users, authorizations, configProvider.adminUsername(), configProvider.adminPassword())

    val router = Router.router(vertx)
      .errorHandler(401) { context -> log.warn("Unauthenticated call received: ${context.request().method()} ${context.request().uri()}") }
      .errorHandler(403) { context -> log.warn("Unauthorized call received: ${context.request().method()} ${context.request().uri()}") }
      .errorHandler(500) { context -> log.error("Internal Server Error", context.failure()) }

    enableCorsRequests(router)

    router.route("/api/login").handler(authProvider.basicAuthenticationHandler())
    router.route("/api/*").handler(authProvider.jwtAuthenticationHandler())
    router.route("/api/trainers/*").handler(authProvider.roleAuthorizationHandler(ADMIN))
    router.route("/api/trainees/*").handler(authProvider.roleAuthorizationHandler(TRAINER))

    PostLoginApi(router, authProvider.jwtAuthentication())
    PostTrainersApi(router, trainers, idProvider, dateTimeProvider, passwordProvider, users, authorizations)
    PostTraineesApi(router, trainees, idProvider, dateTimeProvider, passwordProvider, users, authorizations)

    vertx
      .createHttpServer()
      .requestHandler(router)
      .listen(port())
      .onSuccess { server -> log.info("Workout backend server is listening on port ${server.actualPort()}") }
      .onFailure { log.error("Workout backend server failed to start", it) }
  }

  private fun port(): Int = System.getenv("PORT")?.let { Integer.parseInt(it) } ?: 8085

  private fun setAdminUser(users: MongoUsers, authorizations: MongoAuthorizations, username: String, password: String) {
    users.find(username).onSuccess { userAlreadyExist ->
      if (!userAlreadyExist) { users.add(username, password) }
    }
    authorizations.findRoles(username).onSuccess { roles ->
      if (!roles.contains(ADMIN)) { authorizations.addRole(username, ADMIN)}
    }
  }

  private fun enableCorsRequests(router: Router) {
    val allowedHeaders: MutableSet<String> = HashSet()
    allowedHeaders.add("x-requested-with")
    allowedHeaders.add("Access-Control-Allow-Origin")
    allowedHeaders.add("origin")
    allowedHeaders.add("Content-Type")
    allowedHeaders.add("accept")
    allowedHeaders.add("X-PINGARUNER")
    allowedHeaders.add("Authorization")

    val allowedMethods: MutableSet<HttpMethod> = HashSet()
    allowedMethods.add(HttpMethod.GET)
    allowedMethods.add(HttpMethod.POST)
    allowedMethods.add(HttpMethod.PUT)
    allowedMethods.add(HttpMethod.OPTIONS)
    allowedMethods.add(HttpMethod.DELETE)
    router.route().handler(CorsHandler.create("*").allowedHeaders(allowedHeaders).allowedMethods(allowedMethods))
  }
}

private fun main() {
  Vertx.vertx().deployVerticle(MainVerticle()) { result ->
    when {
      result.succeeded() -> println("Verticle deployed ${result.result()}")
      result.failed() -> println("Verticle NOT deployed ${result.cause()}")
    }
  }
}
