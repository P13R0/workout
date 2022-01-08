package it.anesin.workout

import io.vertx.core.Vertx

fun main() {
  Vertx.vertx().deployVerticle(MainVerticle()) { result ->
    when {
      result.succeeded() -> println("Verticle deployed ${result.result()}")
      result.failed() -> println("Verticle NOT deployed ${result.cause()}")
    }
  }
}

