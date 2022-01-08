package it.anesin.workout

import io.vertx.core.AbstractVerticle
import io.vertx.core.Promise
import io.vertx.core.http.impl.HttpClientConnection.log
import io.vertx.ext.web.Router
import it.anesin.workout.api.PostLoginApi
import java.io.FileNotFoundException
import java.util.*

class MainVerticle : AbstractVerticle() {

  override fun start(startPromise: Promise<Void>) {
    val prop = Properties()
    val inputStream = javaClass.classLoader.getResourceAsStream("config.properties")

    if (inputStream != null) prop.load(inputStream)
    else throw FileNotFoundException("Config file not found in the resources")

    val router = Router.router(vertx)

    PostLoginApi(router, prop.getProperty("username"), prop.getProperty("password"))

    vertx
      .createHttpServer()
      .requestHandler(router)
      .listen(port())
      .onSuccess { server -> log.info("Workout backend server is listening on port ${server.actualPort()}") }
      .onFailure { log.error("Workout backend server failed to start", it) }
  }
}

private fun port(): Int = System.getenv("PORT")?.let { Integer.parseInt(it) } ?: 8085
