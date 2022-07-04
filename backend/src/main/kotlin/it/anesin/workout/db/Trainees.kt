package it.anesin.workout.db

import io.vertx.core.Future
import it.anesin.workout.domain.Trainee

interface Trainees {
  fun add(trainee: Trainee): Future<String>
  fun find(username: String): Future<Trainee?>
  fun findAll(): Future<List<Trainee>>
}
