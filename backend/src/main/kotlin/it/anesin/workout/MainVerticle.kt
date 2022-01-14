package it.anesin.workout

import io.vertx.core.AbstractVerticle
import io.vertx.core.Promise
import io.vertx.core.Vertx
import io.vertx.core.http.impl.HttpClientConnection.log
import io.vertx.core.json.JsonArray
import io.vertx.core.json.JsonObject
import io.vertx.ext.auth.PubSecKeyOptions
import io.vertx.ext.auth.jwt.JWTAuth
import io.vertx.ext.auth.jwt.JWTAuthOptions
import io.vertx.ext.mongo.MongoClient
import io.vertx.ext.web.Router
import io.vertx.ext.web.handler.JWTAuthHandler
import it.anesin.workout.provider.UserRole.*
import it.anesin.workout.api.PostLoginApi
import it.anesin.workout.api.PostTrainersApi
import it.anesin.workout.db.MongoTrainers
import it.anesin.workout.provider.DefaultAuthProvider
import it.anesin.workout.provider.DefaultPasswordProvider
import it.anesin.workout.provider.UTCDateTimeProvider
import it.anesin.workout.provider.UUIDIdProvider
import java.io.FileNotFoundException
import java.util.*

class MainVerticle : AbstractVerticle() {

  override fun start(startPromise: Promise<Void>) {
    JsonMapper.setPreferences()

    val prop = Properties()
    val inputStream = javaClass.classLoader.getResourceAsStream("config.properties")
    if (inputStream != null) prop.load(inputStream)
    else throw FileNotFoundException("Config file not found in the resources")

    val mongoClient = MongoClient.createShared(vertx, mongoConfig(prop))
    val authProvider = DefaultAuthProvider(mongoClient)

    authProvider.addUser(prop.getProperty("admin_username"), prop.getProperty("admin_password"), ADMIN)

    val publicKey = PubSecKeyOptions().setAlgorithm("RS256").setBuffer(prop.getProperty("jwt_public_key"))
    val privateKey = PubSecKeyOptions().setAlgorithm("RS256").setBuffer(prop.getProperty("jwt_private_key"))
    val jwtAuthenticationOptions = JWTAuthOptions().addPubSecKey(publicKey).addPubSecKey(privateKey)
    val jwtAuthentication = JWTAuth.create(vertx, jwtAuthenticationOptions)

    val router = Router.router(vertx)
      .errorHandler(401) { context -> log.warn("Unauthenticated call received: ${context.request().method()} ${context.request().uri()}") }
      .errorHandler(500) { context -> log.error("Internal Server Error", context.failure()) }

    router.route("/api/login").handler(authProvider.basicAuthHandler())

    val jwtAuthHandler = JWTAuthHandler.create(jwtAuthentication)
    router.route("/api/*").handler(jwtAuthHandler)

    val idProvider = UUIDIdProvider()
    val dateTimeProvider = UTCDateTimeProvider()
    val passwordProvider = DefaultPasswordProvider()

    val trainers = MongoTrainers(mongoClient)

    PostLoginApi(router, jwtAuthentication)
    PostTrainersApi(router, trainers, idProvider, dateTimeProvider, authProvider, passwordProvider)

    vertx
      .createHttpServer()
      .requestHandler(router)
      .listen(port())
      .onSuccess { server -> log.info("Workout backend server is listening on port ${server.actualPort()}") }
      .onFailure { log.error("Workout backend server failed to start", it) }
  }

  private fun port(): Int = System.getenv("PORT")?.let { Integer.parseInt(it) } ?: 8085

  private fun mongoConfig(prop: Properties): JsonObject = JsonObject()
    .put("hosts", JsonArray()
      .add(JsonObject().put("host", prop.getProperty("mongo_host_1")).put("port", 27017))
      .add(JsonObject().put("host", prop.getProperty("mongo_host_2")).put("port", 27017))
      .add(JsonObject().put("host", prop.getProperty("mongo_host_3")).put("port", 27017)))
    .put("replicaSet", prop.getProperty("mongo_replica_set"))
    .put("db_name", prop.getProperty("mongo_db_name"))
    .put("username", prop.getProperty("mongo_username"))
    .put("password", prop.getProperty("mongo_password"))
    .put("ssl", true)
    .put("authSource", "admin")
}

private fun main() {
  Vertx.vertx().deployVerticle(MainVerticle()) { result ->
    when {
      result.succeeded() -> println("Verticle deployed ${result.result()}")
      result.failed() -> println("Verticle NOT deployed ${result.cause()}")
    }
  }
}
