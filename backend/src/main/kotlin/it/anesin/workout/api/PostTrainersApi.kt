package it.anesin.workout.api

import io.vertx.core.Future.failedFuture
import io.vertx.core.Handler
import io.vertx.core.http.impl.HttpClientConnection.log
import io.vertx.ext.web.Router
import io.vertx.ext.web.RoutingContext
import io.vertx.ext.web.handler.BodyHandler
import io.vertx.ext.web.handler.HttpException
import it.anesin.workout.db.Trainers
import it.anesin.workout.domain.Trainer
import it.anesin.workout.provider.AuthProvider
import it.anesin.workout.provider.DateTimeProvider
import it.anesin.workout.provider.IdGenerator
import it.anesin.workout.provider.UserRole.TRAINER

class PostTrainersApi(
  router: Router,
  private val trainers: Trainers,
  private val idGenerator: IdGenerator,
  private val dateTimeProvider: DateTimeProvider,
  private val authProvider: AuthProvider
) : Handler<RoutingContext> {

  init {
    router.post("/api/trainers").handler(BodyHandler.create()).handler(this)
  }

  override fun handle(context: RoutingContext) {
    val trainer = context.bodyAsJson.put("_id", idGenerator.random().toString()).put("createdAt", dateTimeProvider.now()).mapTo(Trainer::class.java)

    // TODO: creare un generatore di passwords
    authProvider.addUser(trainer.username, "aPassword", TRAINER)
      .compose { trainers.find(trainer.username) }
      .compose {
        if (it == null) trainers.add(trainer)
        else failedFuture(HttpException(409))
      }
      .onSuccess {
        log.info("Trainer ${trainer.username} with id ${trainer._id} added")
        context.response().end()
      }
      .onFailure(context::fail)
  }
}
