package it.anesin.workout.db

import io.vertx.core.Future

interface Users {
  fun add(username: String, password: String): Future<String>
  fun find(username: String): Future<Boolean>
}
