package it.anesin.workout.api

import io.vertx.core.Future.failedFuture
import io.vertx.core.Future.succeededFuture
import io.vertx.core.Handler
import io.vertx.ext.web.Router
import io.vertx.ext.web.RoutingContext
import io.vertx.ext.web.handler.BodyHandler
import io.vertx.ext.web.handler.HttpException
import it.anesin.workout.db.Authorizations
import it.anesin.workout.db.Trainers
import it.anesin.workout.db.Users
import it.anesin.workout.domain.Trainer
import it.anesin.workout.provider.DateTimeProvider
import it.anesin.workout.provider.IdGenerator
import it.anesin.workout.provider.PasswordProvider
import it.anesin.workout.provider.UserRole.TRAINER

class PostTrainersApi(
  router: Router,
  private val trainers: Trainers,
  private val idGenerator: IdGenerator,
  private val dateTimeProvider: DateTimeProvider,
  private val passwordProvider: PasswordProvider,
  private val users: Users,
  private val authorizations: Authorizations
) : Handler<RoutingContext> {

  init {
    router.post("/api/trainers").handler(BodyHandler.create()).handler(this)
  }

  override fun handle(context: RoutingContext) {
    val trainer = context.bodyAsJson
      .put("_id", idGenerator.random().toString())
      .put("createdAt", dateTimeProvider.now())
      .mapTo(Trainer::class.java)

    users.find(trainer.username)
      .compose { userAlreadyExist ->
        if (userAlreadyExist) succeededFuture()
        else { users.add(trainer.username, passwordProvider.random()) }
      }
      .compose { authorizations.findRoles(trainer.username) }
      .compose { roles ->
        if (roles.contains(TRAINER)) succeededFuture()
        else { authorizations.addRole(trainer.username, TRAINER)}
      }
      .compose { trainers.find(trainer.username) }
      .compose {
        if (it == null) trainers.add(trainer)
        else failedFuture(HttpException(409))
      }
      .onSuccess { context.response().end() }
      .onFailure(context::fail)
  }
}
