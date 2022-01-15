package it.anesin.workout.provider

import io.vertx.core.Future
import io.vertx.core.Promise
import io.vertx.core.Vertx
import io.vertx.core.http.impl.HttpClientConnection.log
import io.vertx.ext.auth.PubSecKeyOptions
import io.vertx.ext.auth.User
import io.vertx.ext.auth.authentication.UsernamePasswordCredentials
import io.vertx.ext.auth.authorization.RoleBasedAuthorization
import io.vertx.ext.auth.jwt.JWTAuth
import io.vertx.ext.auth.jwt.JWTAuthOptions
import io.vertx.ext.auth.mongo.*
import io.vertx.ext.mongo.MongoClient
import io.vertx.ext.web.handler.AuthorizationHandler
import io.vertx.ext.web.handler.BasicAuthHandler
import io.vertx.ext.web.handler.JWTAuthHandler

interface AuthProvider {
  fun addUser(username: String, password: String, role: UserRole): Future<Unit>
}

class DefaultAuthProvider(vertx: Vertx, mongoClient: MongoClient, jwtKeys: Pair<String, String>) : AuthProvider {
  private val mongoAuthenticationOptions = MongoAuthenticationOptions().setCollectionName("users")
  private val mongoAuthentication = MongoAuthentication.create(mongoClient, mongoAuthenticationOptions)
  private val mongoAuthorizationOptions = MongoAuthorizationOptions()
  private val mongoAuthorization = MongoAuthorization.create("provider", mongoClient, mongoAuthorizationOptions)
  private val mongoUserUtil = MongoUserUtil.create(mongoClient, mongoAuthenticationOptions, mongoAuthorizationOptions)

  private val firstKey = PubSecKeyOptions().setAlgorithm("RS256").setBuffer(jwtKeys.first)
  private val secondKey = PubSecKeyOptions().setAlgorithm("RS256").setBuffer(jwtKeys.second)
  private val jwtAuthenticationOptions = JWTAuthOptions().addPubSecKey(firstKey).addPubSecKey(secondKey)
  private val jwtAuthentication = JWTAuth.create(vertx, jwtAuthenticationOptions)

  fun jwtAuthentication() = jwtAuthentication!!
  fun jwtAuthenticationHandler() = JWTAuthHandler.create(jwtAuthentication)!!
  fun basicAuthenticationHandler() = BasicAuthHandler.create(mongoAuthentication)!!
  fun roleAuthorizationHandler(role: UserRole) = AuthorizationHandler.create(RoleBasedAuthorization.create(role.name)).addAuthorizationProvider(mongoAuthorization)!!

  override fun addUser(username: String, password: String, role: UserRole): Future<Unit> {
    val credentials = UsernamePasswordCredentials(username, password)
    val promise = Promise.promise<Unit>()

    mongoAuthentication.authenticate(credentials) { asyncResult ->
      if (asyncResult.succeeded()) {
        addAuthorization(asyncResult.result(), role)
          .onSuccess { succeedPromise(credentials, role, promise) }
          .onFailure { failedPromise(credentials, role, it, promise) }
      } else {
        mongoUserUtil.createUser(credentials.username, credentials.password)
          .compose { mongoAuthentication.authenticate(credentials) }
          .compose { user -> addAuthorization(user, role) }
          .onSuccess { succeedPromise(credentials, role, promise) }
          .onFailure { failedPromise(credentials, role, it, promise) }
      }
    }

    return promise.future()
  }

  private fun succeedPromise(credentials: UsernamePasswordCredentials, role: UserRole, promise: Promise<Unit>) {
    log.info("User ${credentials.username} added or already added with role $role")
    promise.complete()
  }

  private fun failedPromise(credentials: UsernamePasswordCredentials, role: UserRole, it: Throwable?, promise: Promise<Unit>) {
    log.error("Failed to add user ${credentials.username} with role $role", it)
    promise.fail(it)
  }

  private fun addAuthorization(user: User, role: UserRole): Future<String> {
    return mongoAuthorization.getAuthorizations(user)
      .compose {
        if (isRoleAbsent(role, user)) {
          mongoUserUtil.createUserRolesAndPermissions(user.principal().getString("username"), listOf(role.name), listOf())
        } else { Future.succeededFuture() }
      }
  }

  private fun isRoleAbsent(role: UserRole, user: User) = !RoleBasedAuthorization.create(role.name).match(user)
}

enum class UserRole { ADMIN, TRAINER, TRAINEE }
