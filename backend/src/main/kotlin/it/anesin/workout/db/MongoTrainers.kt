package it.anesin.workout.db

import io.vertx.core.Future
import io.vertx.core.Promise.promise
import io.vertx.core.json.JsonObject
import io.vertx.ext.mongo.MongoClient
import it.anesin.workout.domain.Trainer

class MongoTrainers(private val mongoClient: MongoClient) : Trainers {

  override fun add(trainer: Trainer): Future<Unit> {
    val promise = promise<Unit>()

    mongoClient.insert("trainers", JsonObject.mapFrom(trainer)) { asyncResult ->
      if (asyncResult.succeeded()) promise.complete()
      else promise.fail(asyncResult.cause())
    }
    return promise.future()
  }

  override fun findAll(): Future<List<Trainer>> {
    val promise = promise<List<Trainer>>()

    mongoClient.find("trainers", JsonObject()) { asyncResult ->
      if (asyncResult.succeeded()) promise.complete(asyncResult.result().map { it.mapTo(Trainer::class.java) })
      else promise.fail(asyncResult.cause())
    }
    return promise.future()
  }
}
