package it.anesin.workout.db

import io.vertx.core.Future
import io.vertx.core.http.impl.HttpClientConnection.log
import io.vertx.core.json.JsonObject
import io.vertx.ext.auth.mongo.MongoUserUtil
import io.vertx.ext.mongo.MongoClient
import it.anesin.workout.provider.UserRole

class MongoAuthorizations(private val mongoClient: MongoClient, private val mongoUserUtil: MongoUserUtil): Authorizations {

  override fun addRole(username: String, role: UserRole): Future<String> =
    mongoUserUtil.createUserRolesAndPermissions(username, listOf(role.name), listOf())
      .onSuccess { log.info("Added role $role to user $username") }
      .onFailure { log.error("Failed to add role $role to user $username", it) }

  override fun findRoles(username: String): Future<List<UserRole>> =
    mongoClient.findOne("authorizations", JsonObject().put("username", username), JsonObject())
      .map { it?.getJsonArray("roles")?.toList() ?: listOf() }
      .map { it.map { role -> UserRole.valueOf(role.toString()) }}
}
