package it.anesin.workout.api

import io.vertx.core.Future
import io.vertx.core.Future.failedFuture
import io.vertx.core.Future.succeededFuture
import io.vertx.core.Handler
import io.vertx.core.http.impl.HttpClientConnection
import io.vertx.ext.web.Router
import io.vertx.ext.web.RoutingContext
import io.vertx.ext.web.handler.BodyHandler
import io.vertx.ext.web.handler.HttpException
import it.anesin.workout.db.Authorizations
import it.anesin.workout.db.Trainees
import it.anesin.workout.db.Users
import it.anesin.workout.domain.Trainee
import it.anesin.workout.provider.DateTimeProvider
import it.anesin.workout.provider.IdGenerator
import it.anesin.workout.provider.PasswordProvider
import it.anesin.workout.provider.UserRole

class PostTraineesApi(
  router: Router,
  private val trainees: Trainees,
  private val idGenerator: IdGenerator,
  private val dateTimeProvider: DateTimeProvider,
  private val passwordProvider: PasswordProvider,
  private val users: Users,
  private val authorizations: Authorizations
): Handler<RoutingContext> {

  init {
    router.post("/api/trainees").handler(BodyHandler.create()).handler(this)
  }

  override fun handle(context: RoutingContext) {
    val trainee = context.bodyAsJson
      .put("_id", idGenerator.random().toString())
      .put("createdAt", dateTimeProvider.now())
      .mapTo(Trainee::class.java)

    users.find(trainee.username)
      .compose { userAlreadyExist ->
        if (userAlreadyExist) succeededFuture()
        else users.add(trainee.username, passwordProvider.random())
      }
      .compose { authorizations.findRoles(trainee.username) }
      .compose { roles ->
        if (roles.contains(UserRole.TRAINER)) succeededFuture()
        else { authorizations.addRole(trainee.username, UserRole.TRAINER)}
      }
      .compose { trainees.find(trainee.username) }
      .compose {
        if (it == null) trainees.add(trainee)
        else failedFuture(HttpException(409))
      }
      .onSuccess { context.response().end() }
      .onFailure(context::fail)
  }
}
