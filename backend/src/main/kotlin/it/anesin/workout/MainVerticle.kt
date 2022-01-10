package it.anesin.workout

import io.vertx.core.AbstractVerticle
import io.vertx.core.Promise
import io.vertx.core.Vertx
import io.vertx.core.http.impl.HttpClientConnection.log
import io.vertx.core.json.JsonArray
import io.vertx.core.json.JsonObject
import io.vertx.ext.auth.authentication.UsernamePasswordCredentials
import io.vertx.ext.auth.mongo.MongoAuthentication
import io.vertx.ext.auth.mongo.MongoAuthenticationOptions
import io.vertx.ext.auth.mongo.MongoAuthorizationOptions
import io.vertx.ext.auth.mongo.MongoUserUtil
import io.vertx.ext.mongo.MongoClient
import io.vertx.ext.web.Router
import io.vertx.ext.web.handler.BasicAuthHandler
import it.anesin.workout.api.PostLoginApi
import java.io.FileNotFoundException
import java.util.*

class MainVerticle : AbstractVerticle() {

  override fun start(startPromise: Promise<Void>) {
    val prop = Properties()
    val inputStream = javaClass.classLoader.getResourceAsStream("config.properties")

    if (inputStream != null) prop.load(inputStream)
    else throw FileNotFoundException("Config file not found in the resources")

    val mongoClient = MongoClient.createShared(vertx, mongoConfig(prop))
    val mongoAuthenticationOptions = MongoAuthenticationOptions().setCollectionName("users")
    val mongoAuthentication = MongoAuthentication.create(mongoClient, mongoAuthenticationOptions)
    val mongoAuthorizationOptions = MongoAuthorizationOptions()
    val mongoUserUtil = MongoUserUtil.create(mongoClient, mongoAuthenticationOptions, mongoAuthorizationOptions)

    val adminCredentials = UsernamePasswordCredentials(prop.getProperty("admin_username"), prop.getProperty("admin_password"))
    mongoAuthentication.authenticate(adminCredentials).onFailure {
      mongoUserUtil.createUser(adminCredentials.username, adminCredentials.password)
        .onSuccess { log.info("Admin user created") }
        .onFailure { log.error("Admin user creation failed", it)}
    }

    val router = Router.router(vertx)

    val basicAuthHandler = BasicAuthHandler.create(mongoAuthentication)
    router.route("/api/login").handler(basicAuthHandler).failureHandler { it.response().setStatusCode(401).end("Authentication failed") }

    PostLoginApi(router)

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
