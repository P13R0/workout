package it.anesin.workout.api

import io.vertx.core.Future.failedFuture
import io.vertx.core.Handler
import io.vertx.core.http.impl.HttpClientConnection.log
import io.vertx.ext.web.Router
import io.vertx.ext.web.RoutingContext
import io.vertx.ext.web.handler.BodyHandler
import io.vertx.ext.web.handler.HttpException
import it.anesin.workout.DateTimeGenerator
import it.anesin.workout.IdGenerator
import it.anesin.workout.db.Trainers
import it.anesin.workout.domain.Trainer
import java.lang.Exception

class PostTrainersApi(
  router: Router,
  private val trainers: Trainers,
  private val idGenerator: IdGenerator,
  private val dateTimeGenerator: DateTimeGenerator
) : Handler<RoutingContext> {

  init {
    router.post("/api/trainers").handler(BodyHandler.create()).handler(this)
  }

  override fun handle(context: RoutingContext) {
    val trainer = context.bodyAsJson
      .put("_id", idGenerator.random().toString())
      .put("createdAt", dateTimeGenerator.now())
      .mapTo(Trainer::class.java)

    trainers.find(trainer.username)
      .compose {
        if (it == null) trainers.add(trainer)
        else failedFuture(HttpException(409, Exception("Trainer ${trainer.username} can't be added because already exist")))
      }
      .onSuccess {
        log.info("Trainer ${trainer.username} with id ${trainer._id} added")
        context.response().end()
      }
      .onFailure(context::fail)
  }
}
