package it.anesin.workout.db

import io.vertx.core.Future
import it.anesin.workout.provider.UserRole

interface Authorizations {
  fun addRole(username: String, role: UserRole): Future<String>
  fun findRoles(username: String): Future<List<UserRole>>
}
