package it.anesin.workout

import com.typesafe.config.ConfigFactory
import io.vertx.core.AbstractVerticle
import io.vertx.core.Promise
import io.vertx.core.Vertx
import io.vertx.core.http.impl.HttpClientConnection.log
import io.vertx.ext.web.Router
import it.anesin.workout.api.PostLoginApi
import java.io.File

class MainVerticle : AbstractVerticle() {

  override fun start(startPromise: Promise<Void>) {
    val config = ConfigFactory.parseFile(File("backend/app.conf"))
    val adminCredentials = config.getConfig("admin")

    val router = Router.router(vertx)

    PostLoginApi(router, adminCredentials)

    vertx
      .createHttpServer()
      .requestHandler(router)
      .listen(port())
      .onSuccess { server -> log.info("Workout backend server is listening on port ${server.actualPort()}") }
      .onFailure { log.error("Workout backend server failed to start", it) }
  }
}

private fun port(): Int = System.getenv("PORT")?.let { Integer.parseInt(it) } ?: 8080

private fun main() {
  val vertx = Vertx.vertx()
  vertx.deployVerticle(MainVerticle())
}
