package it.anesin.workout.db

import io.vertx.core.Future
import io.vertx.core.http.impl.HttpClientConnection.log
import io.vertx.core.json.JsonObject
import io.vertx.ext.mongo.MongoClient
import it.anesin.workout.domain.Trainer

class MongoTrainers(private val mongoClient: MongoClient) : Trainers {

  override fun add(trainer: Trainer): Future<String> =
    mongoClient.insert("trainers", JsonObject.mapFrom(trainer))
      .onSuccess { log.info("Added trainer ${trainer.username}") }
      .onFailure { log.error("Failed to add trainer ${trainer.username}", it) }

  override fun find(username: String): Future<Trainer?> =
    mongoClient.findOne("trainers", JsonObject().put("username", username), JsonObject())
      .map { it?.mapTo(Trainer::class.java) }
}
