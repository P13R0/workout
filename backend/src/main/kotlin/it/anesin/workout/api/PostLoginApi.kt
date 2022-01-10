package it.anesin.workout.api

import io.netty.handler.codec.http.HttpHeaderNames.CONTENT_TYPE
import io.netty.handler.codec.http.HttpHeaderValues.APPLICATION_JSON
import io.vertx.core.Handler
import io.vertx.core.json.JsonObject
import io.vertx.ext.web.Router
import io.vertx.ext.web.RoutingContext

class PostLoginApi(router: Router) : Handler<RoutingContext>{

  init {
    router.post("/api/login").handler(this)
  }

  override fun handle(context: RoutingContext) {
      context.response()
        .putHeader(CONTENT_TYPE, APPLICATION_JSON)
        .end(JsonObject().put("token", "aToken").toBuffer())
  }
}
