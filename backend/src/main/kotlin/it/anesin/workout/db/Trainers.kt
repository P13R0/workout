package it.anesin.workout.db

import io.vertx.core.Future
import it.anesin.workout.domain.Trainer

interface Trainers {
  fun add(trainer: Trainer): Future<String>
  fun find(username: String): Future<Trainer?>
}
