package it.anesin.workout.db

import io.vertx.core.Future
import io.vertx.core.Promise.promise
import io.vertx.core.http.impl.HttpClientConnection
import io.vertx.core.http.impl.HttpClientConnection.*
import io.vertx.core.json.JsonObject
import io.vertx.ext.mongo.MongoClient
import it.anesin.workout.domain.Trainee

class MongoTrainees(private val mongoClient: MongoClient) : Trainees {

  override fun add(trainee: Trainee): Future<String> =
    mongoClient.insert("trainees", JsonObject.mapFrom(trainee))
      .onSuccess { log.info("Added trainee ${trainee.username}") }
      .onFailure { log.error("Failed to add trainee ${trainee.username}", it) }

  override fun find(username: String): Future<Trainee?> =
    mongoClient.findOne("trainees", JsonObject().put("username", username), JsonObject())
      .map { it?.mapTo(Trainee::class.java) }
}
