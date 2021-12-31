package it.anesin.workout.api

import com.typesafe.config.Config
import io.netty.handler.codec.http.HttpHeaderNames.CONTENT_TYPE
import io.netty.handler.codec.http.HttpHeaderValues.APPLICATION_JSON
import io.vertx.core.Handler
import io.vertx.core.json.JsonObject
import io.vertx.ext.web.Router
import io.vertx.ext.web.RoutingContext
import io.vertx.ext.web.handler.BodyHandler

class PostLoginApi(router: Router, private val adminCredentials: Config) : Handler<RoutingContext>{
  init {
    router.post("/api/login").handler(BodyHandler.create()).handler(this)
  }

  override fun handle(context: RoutingContext) {
    val body = context.bodyAsJson
    val username = body.getString("username")
    val password = body.getString("password")

    if (username == adminCredentials.getString("username") && password == adminCredentials.getString("password")) {
      context.response()
        .putHeader(CONTENT_TYPE, APPLICATION_JSON)
        .end(JsonObject().put("token", "aToken").toBuffer())
    }
    else {
      context.response().end()
    }
  }
}
