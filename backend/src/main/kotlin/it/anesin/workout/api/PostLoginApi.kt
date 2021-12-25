package it.anesin.workout.api

import io.vertx.core.Handler
import io.vertx.ext.web.Router
import io.vertx.ext.web.RoutingContext

class PostLoginApi(val router: Router) : Handler<RoutingContext>{
  init {
    router.post("/api/login").handler(this)
  }

  override fun handle(context: RoutingContext) {
    context.response().end()
  }
}
