package it.anesin.workout.db

import io.vertx.core.Future
import io.vertx.core.Promise.promise
import io.vertx.core.http.impl.HttpClientConnection.log
import io.vertx.core.json.JsonObject
import io.vertx.ext.auth.User
import io.vertx.ext.auth.authentication.AuthenticationProvider
import io.vertx.ext.auth.mongo.MongoUserUtil
import io.vertx.ext.mongo.MongoClient

class MongoUsers(private val mongoClient: MongoClient, private val mongoUserUtil: MongoUserUtil) : Users {

  override fun add(username: String, password: String): Future<String> =
    mongoUserUtil.createUser(username, password)
      .onSuccess { log.info("Added user $username with id $it") }
      .onFailure { log.error("Failed to add user $username", it) }

  override fun find(username: String): Future<Boolean> =
    mongoClient.findOne("users", JsonObject().put("username", username), JsonObject()).map { it != null }
}
