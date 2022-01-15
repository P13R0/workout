package it.anesin.workout.api

import io.netty.handler.codec.http.HttpHeaderNames.CONTENT_TYPE
import io.netty.handler.codec.http.HttpHeaderValues.APPLICATION_JSON
import io.vertx.core.Handler
import io.vertx.core.json.JsonObject
import io.vertx.ext.auth.JWTOptions
import io.vertx.ext.auth.User
import io.vertx.ext.auth.jwt.JWTAuth
import io.vertx.ext.web.Router
import io.vertx.ext.web.RoutingContext

class PostLoginApi(router: Router, private val jwtProvider: JWTAuth) : Handler<RoutingContext>{

  init {
    router.post("/api/login").handler(this)
  }

  override fun handle(context: RoutingContext) {
    val oneDayInMinutes = 1440
    val options = JWTOptions().setAlgorithm("RS256").setExpiresInMinutes(oneDayInMinutes)
    val token = jwtProvider.generateToken(context.user().principal().putNull("password"), options)

    context.response()
      .putHeader(CONTENT_TYPE, APPLICATION_JSON)
      .end(JsonObject().put("token", token).toBuffer())
  }
}
