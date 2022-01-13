package it.anesin.workout.db

import io.vertx.core.Future
import io.vertx.core.json.JsonObject
import it.anesin.workout.domain.Trainer

interface Trainers {
  fun add(trainer: Trainer): Future<Unit>
  fun find(username: String): Future<Trainer?>
  fun findAll(): Future<List<Trainer>>
}
